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
import reactor.core.publisher.Mono;
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

    private boolean webSocketConnected;

    private WebSocketSession session;

    private ReactiveWebSocketConnection(WebSocketCallback callback) {
      this.callback = callback;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
      this.session = session;

      /* handles incoming messages */
      final Flux<String> receive = session
          .receive()
          .map(WebSocketMessage::getPayloadAsText)
          .doOnNext(message -> {
            logger.debug("<<< {}", message);
            callback.onMessage(message);
          });

      final Mono<Object> connected = Mono.fromRunnable(() -> {
        webSocketConnected = true;
        callback.connected(session.getId());
      });

      final Mono<Object> disconnected = Mono.fromRunnable(() -> {
        webSocketConnected = false;
        callback.disconnected(session.getId());
      });

      return connected
          .thenMany(receive)
          .then(disconnected)
          .then();
    }

    void send(String message) {
      if (webSocketConnected) {
        logger.debug(">>> {}", message);
        
        session
            .send(Mono.just(session.textMessage(message)))
            .subscribe();
      }
    }

    boolean isWebSocketConnected() {
      return webSocketConnected;
    }
  }
}
