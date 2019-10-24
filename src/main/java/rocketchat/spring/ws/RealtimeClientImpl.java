package rocketchat.spring.ws;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import rocketchat.spring.ClientProperties;
import rocketchat.spring.model.Subscription;
import rocketchat.spring.ws.events.ClientStartedEvent;
import rocketchat.spring.ws.events.Events;
import rocketchat.spring.ws.events.RocketChatEvent;
import rocketchat.spring.ws.events.UserAwareEvent;
import rocketchat.spring.ws.messages.IdentityAware;
import rocketchat.spring.ws.messages.Message;
import rocketchat.spring.ws.messages.Messages;
import rocketchat.spring.ws.messages.Parsers;
import rocketchat.spring.ws.socket.ReactiveWebSocket;
import rocketchat.spring.ws.socket.WebSocket;
import rocketchat.spring.ws.socket.WebSocketCallback;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * Default {@link RealtimeClient} implementation
 */
public class RealtimeClientImpl implements RealtimeClient {
  private static final Logger log = LoggerFactory.getLogger(RealtimeClientImpl.class);

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

  private volatile SecurityContext securityContext;

  public RealtimeClientImpl(WebSocketClient webSocketClient,
                            ClientProperties properties,
                            ApplicationEventPublisher eventPublisher) {
    this.properties = properties;
    this.eventPublisher = eventPublisher;
    this.socket = new ReactiveWebSocket(webSocketClient, new SocketHandler());
  }

  @Override
  public void start() {
    socket.connect(properties.webSocketUri());
  }

  @Override
  public void getSubscriptions(Consumer<Subscription> consumer) {
    send(Messages.joinedRooms(), jsonNode ->
        Parsers.GetSubscriptionsResponseParser.parse(jsonNode).forEach(consumer));
  }

  @Override
  public void sendMessage(String roomId, String message) {
    send(Messages.sendTextMessage(roomId, message));
  }

  @Override
  public void streamRoomMessages(String roomId, Consumer<String> subscriptionId) {
    send(Messages.streamRoomMessages(roomId), json -> {
      if (subscriptionId != null) {
        subscriptionId.accept(json.get("subs").get(0).asText());
      }
    });
  }

  @Override
  public void streamPersonalMessages(Consumer<String> subscriptionId) {
    streamRoomMessages("__my_messages__", subscriptionId);
  }

  @Override
  public void streamSubscriptionChanges() {
    send(Messages.streamNotifyUser(securityContext.userId, "subscriptions-changed"));
  }

  @Override
  public void cancelStream(String id) {
    send(Messages.unsubscribe(id));
  }

  /**
   * Adds the provided message to the outgoing messages queue. The message will be sent asynchronously at some point in
   * the future
   */
  private void send(Message message) {
    send(message, null);
  }

  /**
   * Adds the provided message to the outgoing messages queue. The message will be sent asynchronously at some point in
   * the future. The provided {@link Consumer} instance will be called with raw json response if it could be matched
   * with the request
   */
  private void send(Message message, Consumer<JsonNode> replyHandler) {
    if (message instanceof IdentityAware) {
      final IdentityAware identityAware = (IdentityAware) message;
      identityAware.setId(String.valueOf(generator.incrementAndGet()));

      this.replyMatcher.add(identityAware, replyHandler);
    }
    final String msg = Messages.toJsonString(message);

    socket.send(msg);
  }

  private void setSecurityContext(SecurityContext securityContext) {
    this.securityContext = securityContext;
  }

  /**
   * WebSocketCallback implementation for the underlying {@link WebSocket instance}
   */
  private class SocketHandler implements WebSocketCallback {

    @Override
    public void connected(String sessionId) {

      /* Initial connect message, has to be sent otherwise the server will close connection */
      send(Messages.connect());

      /* Automatically login with provided credentials */
      send(Messages.login(properties.getUser(), properties.getPassword()),
          json -> {
            if (json.hasNonNull("error")) {
              throw new RuntimeException(JsonUtils.getText(json.get("error"), "message"));
            }
            setSecurityContext(SecurityContext.fromLoginResponse(json));

            eventPublisher.publishEvent(new ClientStartedEvent());

          });
    }

    @Override
    public void onMessage(String message) {
      final JsonNode json = Messages.parse(message);

      final String msg = JsonUtils.getMsg(json);

      if (msg != null) {
        if ("ping".equals(msg)) {
          socket.send(Messages.pong());
          return;
        }

        replyMatcher.match(json).ifPresent(consumer -> consumer.accept(json));

        final RocketChatEvent event = Events.parse(json);
        if (event != null) {
          //todo: provide better implementation!
          if (event instanceof UserAwareEvent &&
              ((UserAwareEvent) event).getUser().getLogin().equals(properties.getUser())) {
            return; //todo:
          }
          eventPublisher.publishEvent(event);
        }
      }
    }

    @Override
    public void disconnected(String sessionId) {
      log.info("Reconnecting..."); //todo: count reconnection attempts and apply delay
      start();
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
}
