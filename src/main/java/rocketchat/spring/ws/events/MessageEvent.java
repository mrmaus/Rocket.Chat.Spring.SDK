package rocketchat.spring.ws.events;

/**
 * Incoming chat message event
 */
public class MessageEvent implements UserAwareEvent {

  private final String roomId;
  private final String message;
  private final User user;

  MessageEvent(String roomId, String message, User user) {
    this.roomId = roomId;
    this.message = message;
    this.user = user;
  }

  public String getRoomId() {
    return roomId;
  }

  public String getMessage() {
    return message;
  }

  public User getUser() {
    return user;
  }

  //todo; timestamp
  //todo: mentions

  @Override
  public String toString() {
    return "MessageEvent{" +
        "roomId='" + roomId + '\'' +
        ", message='" + message + '\'' +
        ", user=" + user +
        '}';
  }
}
