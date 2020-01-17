package rocketchat.spring.model;

/**
 * Room types according to https://rocket.chat/docs/developer-guides/realtime-api/the-room-object/
 */
public enum RoomType {

  /**
   * Represents 1-to-1 direct message: https://rocket.chat/docs/user-guides/channels/#direct-messages
   */
  DIRECT_MESSAGE("d"),

  /**
   * Public channel https://rocket.chat/docs/user-guides/channels/#public-channels
   */
  PUBLIC_CHANNEL("c"),

  /**
   * https://rocket.chat/docs/user-guides/channels/#private-groups
   */
  PRIVATE_GROUP("p"),

  LIVECHAT("l"),

  UNKNOWN("");

  private final String value;

  RoomType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static RoomType parse(String s) {
    for (RoomType type : values()) {
      if (type.value.equals(s)) {
        return type;
      }
    }
    return UNKNOWN;
  }
}
