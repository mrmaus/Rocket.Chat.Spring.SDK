package rocketchat.spring.ws;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import rocketchat.spring.ClientProperties;
import rocketchat.spring.ws.events.ClientStartedEvent;
import rocketchat.spring.ws.events.Events;
import rocketchat.spring.ws.events.RocketChatEvent;
import rocketchat.spring.ws.events.UserAwareEvent;
import rocketchat.spring.ws.messages.IdentityAware;
import rocketchat.spring.ws.messages.Message;
import rocketchat.spring.ws.messages.Messages;
import rocketchat.spring.ws.socket.ReactiveWebSocket;
import rocketchat.spring.ws.socket.WebSocket;
import rocketchat.spring.ws.socket.WebSocketCallback;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * Base class for {@link RealtimeClient} implementation that contains only connection/messages/socket handling
 */
abstract class ReactiveRealtimeClient implements RealtimeClient, WebSocketCallback {
  private static final Logger log = LoggerFactory.getLogger(ReactiveRealtimeClient.class);

  private final WebSocket socket;

  /**
   * Unique ID generator for messages that must provide it
   */
  private final AtomicLong generator = new AtomicLong(0);

  /**
   * Matches request with response messages if required
   */
  private final ReplyMatcher replyMatcher = new ReplyMatcher();

  private final ClientProperties properties;

  /**
   * {@link ApplicationEventPublisher} that will be used to publish all {@link RocketChatEvent}
   */
  private final ApplicationEventPublisher eventPublisher;

  private final RealtimeExecutorFactory executorFactory;

  private volatile SecurityContext securityContext;

  /**
   * Incoming messages processing is offloaded to this pool
   */
  private ExecutorService messageHandlerService;

  /**
   * Connection indicator
   */
  private AtomicBoolean connected = new AtomicBoolean(false);

  ReactiveRealtimeClient(WebSocketClient webSocketClient,
                         ClientProperties properties,
                         ApplicationEventPublisher eventPublisher,
                         RealtimeExecutorFactory executorFactory) {
    this.properties = properties;
    this.eventPublisher = eventPublisher;
    this.socket = new ReactiveWebSocket(webSocketClient, this);
    this.executorFactory = executorFactory;
  }

  @Override
  public void start() {
    socket.connect(properties.webSocketUri());
  }

  @Override
  public void stop() {
    socket.disconnect();
  }

  @Override
  public boolean isConnected() {
    return connected.get();
  }

  /**
   * Adds the provided message to the outgoing messages queue. The message will be sent asynchronously at some point in
   * the future
   */
  void send(Message message) {
    send(message, null);
  }

  /**
   * Adds the provided message to the outgoing messages queue. The message will be sent asynchronously at some point in
   * the future. The provided {@link Consumer} instance will be called with raw json response if it could be matched
   * with the request
   */
  void send(Message message, Consumer<JsonNode> replyHandler) {
    if (message instanceof IdentityAware) {
      final IdentityAware identityAware = (IdentityAware) message;
      identityAware.setId(String.valueOf(generator.incrementAndGet()));

      this.replyMatcher.add(identityAware, replyHandler);
    }

    if (message instanceof Messages.ConnectMessage) {  //special handling for initial Connect message
      this.replyMatcher.add("connect", replyHandler);
    }

    final String msg = Messages.toJsonString(message);

    socket.send(msg);
  }

  private void setSecurityContext(SecurityContext securityContext) {
    this.securityContext = securityContext;
  }

  @Override
  public void connected(String sessionId) {
    connected.set(true);

    messageHandlerService = this.executorFactory.create();

    log.info("Connection established successfully");

    /* Initial connect message, has to be sent otherwise the server will close connection */
    send(Messages.connect(), connectReply -> {

      /* Automatically login with provided credentials */
      send(Messages.login(properties.getUser(), properties.getPassword()),
          json -> {
            if (json.hasNonNull("error")) {
              throw new RuntimeException(JsonUtils.getText(json.get("error"), "message"));
            }
            setSecurityContext(SecurityContext.fromLoginResponse(json));

            eventPublisher.publishEvent(new ClientStartedEvent());
          });
    });
  }

  @Override
  public void onMessage(String message) {
    final JsonNode json = Messages.parse(message);

    final String msg = JsonUtils.getMsg(json);

    if ("ping".equals(msg)) {
      socket.send(Messages.pong());
      return;
    }

    messageHandlerService.submit(() -> {
      replyMatcher.match(json).ifPresent(consumer -> consumer.accept(json));

      final RocketChatEvent event = Events.parse(json);
      if (event != null) {
        //todo: provide better implementation!
        if (event instanceof UserAwareEvent &&
            ((UserAwareEvent) event).getUser().getLogin().equals(properties.getUser())) {
          return; //todo:
        }
        try {
          eventPublisher.publishEvent(event);
        } catch (Exception e) {
          log.error("Event publish failed", e);
        }
      }
    });
  }

  @Override
  public void disconnected(String sessionId) {
    connected.set(false);

    messageHandlerService.shutdownNow();

    int counter = 0;

    while (!connected.get()) {
      try {
        Thread.sleep(10_000);
      } catch (InterruptedException e) {
        log.error("Connection thread interrupted", e);
      }
      if (!connected.get()) {
        log.info("Reconnecting {} ...", ++counter);
        start();
      }
    }
  }


  /**
   * Container for authenticated user attributes
   */
  static class SecurityContext {
    private final String userId;
    private final String authToken;
    private final Date tokenExpires;

    private SecurityContext(String userId, String authToken, Date tokenExpires) {
      this.userId = userId;
      this.authToken = authToken;
      this.tokenExpires = tokenExpires;
    }

    static SecurityContext fromLoginResponse(JsonNode json) {
      final JsonNode node = json.get("result");
      return new SecurityContext(JsonUtils.getText(node, "id"),
          JsonUtils.getText(node, "token"),
          JsonUtils.getDate(node, "tokenExpires"));
    }
  }

  protected String userId() {
    return this.securityContext != null ? securityContext.userId : null;
  }
}
