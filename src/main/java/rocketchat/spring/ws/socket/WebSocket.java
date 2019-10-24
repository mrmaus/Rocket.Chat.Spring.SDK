package rocketchat.spring.ws.socket;

import java.net.URI;

/**
 * Low-level websocket client
 */
public interface WebSocket {

  /**
   * Establishes connection to the specified URL
   */
  void connect(URI uri);

  /**
   * Send provided message to the socket
   */
  void send(String message);

  /**
   * Terminate websocket connection
   */
  void disconnect();

  /**
   * @return true if the current socket is connected (can send and receive messages), false otherwise
   */
  boolean isConnected();
}
