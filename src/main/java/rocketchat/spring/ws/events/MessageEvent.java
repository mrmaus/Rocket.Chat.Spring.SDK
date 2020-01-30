package rocketchat.spring.ws.events;

import rocketchat.spring.model.RoomType;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Incoming chat message event
 */
public class MessageEvent implements UserAwareEvent {

  private final String roomId;
  private final String message;
  private final User user;
  private final boolean roomParticipant;
  private final RoomType roomType;
  private final MessageType messageType;
  private final String roomName;
  private final List<User> mentions;

  private MessageEvent(String roomId,
                       String message,
                       User user,
                       boolean roomParticipant,
                       RoomType roomType,
                       MessageType messageType,
                       String roomName,
                       List<User> mentions) {
    this.roomId = roomId;
    this.message = message;
    this.user = user;
    this.roomParticipant = roomParticipant;
    this.roomType = roomType;
    this.messageType = messageType;
    this.roomName = roomName;
    this.mentions = mentions;
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

  public MessageType getMessageType() {
    return messageType;
  }

  public String getRoomName() {
    return roomName;
  }

  public List<User> getMentions() {
    return mentions == null ? Collections.emptyList() : mentions;
  }

  /**
   * @return true if user with provided login is mentioned in this message; false otherwise
   */
  public boolean isMentioned(String login) {
    if (this.mentions == null || login == null) {
      return false;
    }
    return this.mentions.stream().anyMatch(u -> u != null && login.equals(u.getLogin()));
  }

  //todo; timestamp

  @Override
  public String toString() {
    return "MessageEvent{" +
        "roomId='" + roomId + '\'' +
        ", messageType='" + messageType + '\'' +
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
    private MessageType messageType;
    private String roomName;
    private List<User> mentions;

    public MessageEvent build() {
      return new MessageEvent(
          roomId,
          message,
          user,
          roomParticipant,
          roomType,
          messageType,
          roomName,
          mentions);
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

    public Builder messageType(MessageType messageType) {
      this.messageType = messageType;
      return this;
    }

    public Builder roomName(String roomName) {
      this.roomName = roomName;
      return this;
    }

    public Builder addMention(User user) {
      if (this.mentions == null) {
        this.mentions = new LinkedList<>();
      }
      this.mentions.add(user);
      return this;
    }
  }

  public static enum MessageType {
    USER_JOINED_CHANNEL("uj"),
    USER_LEFT("ul"),
    USER_ADDED_BY("au"),
    ROOM_NAME_CHANGED("r"),
    USER_REMOVED_BY("ru"),
    WELCOME("wm"),
    CONVERSATION_FINISHED("livechat-close");

    private final String value;

    MessageType(String value) {
      this.value = value;
    }

    static MessageType parse(String s) {
      for (MessageType type : values()) {
        if (type.value.equals(s)) {
          return type;
        }
      }
      return null;
    }
  }

}
