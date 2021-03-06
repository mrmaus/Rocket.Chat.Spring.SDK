package rocketchat.spring.ws;

import rocketchat.spring.model.Subscription;

import java.util.function.Consumer;

/**
 * client for RocketChat Realtime API https://rocket.chat/docs/developer-guides/realtime-api/
 */
public interface RealtimeClient {

  /**
   * Opens websocket connection, initializes internal handlers and queues. This method must be called once prior any
   * other method in this component
   */
  void start();

  /**
   * Disconnects the underlying socket
   */
  void stop();

  /**
   * @return true if client has successfully established connection; false otherwise
   */
  boolean isConnected();

  /**
   * https://rocket.chat/docs/developer-guides/realtime-api/method-calls/get-subscriptions/
   * <p>
   * Retrieves list of subscriptions available to the current user. The consumer argument will be called once for each
   * returned subscription
   */
  void getSubscriptions(Consumer<Subscription> consumer);

  /**
   * Send simple text message to the specified room. The bot user must be added/invited to the room in order to be able
   * to send messages there
   */
  void sendMessage(String roomId, String message);

  /**
   * Send message to the message thread
   */
  void sendThreadMessage(String roomId, String threadMessageId, String message);

  /**
   * Starts listening for the room messages stream. Optional subscriptionId callback receives the stream ID, which can
   * be user later to cancel stream with {@link #cancelStream(String)}
   */
  void streamRoomMessages(String roomId, Consumer<String> subscriptionId);

  /**
   * Starts listening to all available messages feeds
   */
  void streamMessages(Consumer<String> subscriptionId);

  /**
   * Starts listening to all subscription changes events (user invited to the room, user removed from the room, etc)
   * <p>
   * See 'subscriptions-changed' in https://rocket.chat/docs/developer-guides/realtime-api/subscriptions/stream-notify-user/
   */
  void streamSubscriptionChanges();

  /**
   * 'rooms-changed' event
   * <p>
   * https://rocket.chat/docs/developer-guides/realtime-api/subscriptions/stream-notify-user/
   */
  void streamRoomsChanges();

  /**
   * Cancel previously created listening stream with the specified ID
   */
  void cancelStream(String id);

  /**
   * Creates new DirectMessage https://rocket.chat/docs/developer-guides/realtime-api/method-calls/create-direct-message/
   * The method itself doesn't send any message, but rather creates 'direct' chatroom and returns the room ID, which can
   * be used to send direct message. It is safe to call this method many times for the same user, the server will return
   * same RoomID in this case.
   * <p>
   * Example of sending direct message:
   * <pre>
   *         client.createDirectMessage("user_login", roomID ->
   *           client.sendMessage(roomID, "hello world!"));
   * </pre>
   *
   * @param login the user login to send message to
   */
  void createDirectMessage(String login, Consumer<String> roomId);

  /**
   * Sends a typing event to the specified room ('user is typing...' note appears in the chat)
   */
  void startTyping(String roomId);

  /**
   * Removes typing event from the specified room
   *
   * @see #startTyping(String)
   */
  void stopTyping(String roomId);
}
