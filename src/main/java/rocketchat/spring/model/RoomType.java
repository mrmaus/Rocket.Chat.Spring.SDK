package rocketchat.spring.model;

/**
 * Room types according to https://rocket.chat/docs/developer-guides/realtime-api/the-room-object/
 */
public enum RoomType {

  DIRECT_CHAT("d"),

  CHAT("c"),

  PRIVATE_CHAT("p"),

  LIVECHAT("l");

  private final String value;

  RoomType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
