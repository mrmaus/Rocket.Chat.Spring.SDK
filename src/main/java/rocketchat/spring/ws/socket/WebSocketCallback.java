package rocketchat.spring.ws.socket;

/**
 * callback triggered on important websocket events
 */
public interface WebSocketCallback {

  /**
   * Called when WebSocket connection is successfully established
   */
  void connected(String sessionId);

  /**
   * Message received
   */
  void onMessage(String message);

  /**
   * Connection was terminated by the server
   */
  void disconnected(String sessionId);
}
