package rocketchat.spring.rest.messages;

public class ChannelInfo extends FieldQuery {

  private ChannelInfo(String fieldName, String fieldValue) {
    super(fieldName, fieldValue);
  }

  public static ChannelInfo byRoomId(String id) {
    return new ChannelInfo("roomId", id);
  }

  public static ChannelInfo byRoomName(String name) {
    return new ChannelInfo("roomName", name);
  }
}
