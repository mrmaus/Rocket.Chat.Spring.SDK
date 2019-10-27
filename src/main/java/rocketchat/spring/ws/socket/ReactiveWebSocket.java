package rocketchat.spring.ws.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;
import reactor.core.scheduler.Schedulers;

import java.net.URI;

/**
 * Low-level websocket client implementation using spring reactor
 * <p>
 * Big Thanks (and star) to Luis Moral Guerrero for providing reference implementation and inspiration
 * https://github.com/luis-moral/sample-webflux-websocket-netty
 */
public class ReactiveWebSocket implements WebSocket {
  private static final Logger logger = LoggerFactory.getLogger(ReactiveWebSocket.class);

  private final WebSocketClient webSocketClient;
  private final WebSocketCallback callback;

  /**
   * Session instance that represents currently active connection; new session instance will be created on each
   * connection attempt
   */
  private ReactiveWebSocketConnection session;

  private Disposable _connection;

  public ReactiveWebSocket(WebSocketClient webSocketClient, WebSocketCallback callback) {
    Assert.notNull(webSocketClient, "WebSocketClient parameter can't be null");
    Assert.notNull(callback, "WebSocketCallback parameter can't be null");

    this.webSocketClient = webSocketClient;
    this.callback = callback;
  }

  @Override
  public void connect(URI uri) {
    session = new ReactiveWebSocketConnection(callback);

    this._connection =
        webSocketClient
            .execute(uri, session)
            .subscribeOn(Schedulers.elastic())
            .subscribe();
  }

  @Override
  public void disconnect() {
    if (this._connection != null) {
      this._connection.dispose();
    }
  }

  @Override
  public void send(String message) {
    session.send(message);
  }

  @Override
  public boolean isConnected() {
    return session.isWebSocketConnected();
  }

  /**
   * Container that represents active connection. The instance must be re-created on each connection attempt
   */
  private static class ReactiveWebSocketConnection implements WebSocketHandler {
    private final WebSocketCallback callback;

    /**
     * Gateway for all outgoing messages
     */
    private final UnicastProcessor<String> sendProcessor = UnicastProcessor.create();

    /**
     * Sink for sendProcessor to manually introduce messages to the pipeline
     */
    private final FluxSink<String> sendSink = sendProcessor.sink();

    private boolean webSocketConnected;

    private ReactiveWebSocketConnection(WebSocketCallback callback) {
      this.callback = callback;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) { //todo: fix schedulers issue

      /* handles incoming messages */
      final Flux<String> receive = session
          .receive()
          .map(WebSocketMessage::getPayloadAsText)
          .publishOn(Schedulers.newElastic("incoming-messages-handler"))
          .doOnNext(message -> {
            logger.debug("<<< {}", message);
            callback.onMessage(message);
          });

      /* handles outgoing messages */
      final Flux<WebSocketMessage> send = Flux.from(sendProcessor)
          .doOnNext(msg -> logger.debug(">>> {}", msg))
          .map(session::textMessage);
      ////            .doOnError(AbortedException.class, t -> connectionClosed()) //todo:
//            .doOnError(ClosedChannelException.class, t -> connectionClosed())
//            .onErrorResume(ClosedChannelException.class, t -> Mono.empty())
////            .onErrorResume(AbortedException.class, t -> Mono.empty()) //todo:

      final Mono<Object> connected = Mono.fromRunnable(() -> {
        webSocketConnected = true;
        callback.connected(session.getId());
      });

      final Mono<Object> disconnected = Mono.fromRunnable(() -> {
        webSocketConnected = false;
//        disconnectedProcessor.onNext(session);
        callback.disconnected(session.getId());
      });

      return connected
          .thenMany(Flux.merge(receive, session.send(send)))
          .then(disconnected)
          .then();
    }

    void send(String message) {
      if (webSocketConnected) {
        sendSink.next(message);
      }
    }

    boolean isWebSocketConnected() {
      return webSocketConnected;
    }
  }
}
