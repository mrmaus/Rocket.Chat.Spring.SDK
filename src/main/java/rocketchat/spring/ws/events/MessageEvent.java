package rocketchat.spring.ws.events;

import rocketchat.spring.model.RoomType;

/**
 * Incoming chat message event
 */
public class MessageEvent implements UserAwareEvent {

  private final String roomId;
  private final String message;
  private final User user;
  private final boolean roomParticipant;
  private final RoomType roomType;
  private final String roomName;

  private MessageEvent(String roomId,
                       String message,
                       User user,
                       boolean roomParticipant,
                       RoomType roomType,
                       String roomName) {
    this.roomId = roomId;
    this.message = message;
    this.user = user;
    this.roomParticipant = roomParticipant;
    this.roomType = roomType;
    this.roomName = roomName;
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

  public boolean isRoomParticipant() {
    return roomParticipant;
  }

  public RoomType getRoomType() {
    return roomType;
  }

  public String getRoomName() {
    return roomName;
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

  public static class Builder {
    private String roomId;
    private String message;
    private User user;
    private boolean roomParticipant = true;
    private RoomType roomType = RoomType.UNKNOWN;
    private String roomName;

    public MessageEvent build() {
      return new MessageEvent(
          roomId,
          message,
          user,
          roomParticipant,
          roomType,
          roomName);
    }

    public Builder roomId(String roomId) {
      this.roomId = roomId;
      return this;
    }

    public Builder message(String message) {
      this.message = message;
      return this;
    }

    public Builder user(User user) {
      this.user = user;
      return this;
    }

    public Builder roomParticipant(boolean roomParticipant) {
      this.roomParticipant = roomParticipant;
      return this;
    }

    public Builder roomType(RoomType roomType) {
      this.roomType = roomType;
      return this;
    }

    public Builder roomName(String roomName) {
      this.roomName = roomName;
      return this;
    }
  }
}
