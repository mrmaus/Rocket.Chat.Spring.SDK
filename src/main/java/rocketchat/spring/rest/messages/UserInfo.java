package rocketchat.spring.rest.messages;

public class UserInfo extends FieldQuery {
  private UserInfo(String fieldName, String fieldValue) {
    super(fieldName, fieldValue);
  }

  public static UserInfo byUserId(String id) {
    return new UserInfo("userId", id);
  }

  public static UserInfo byUsername(String name) {
    return new UserInfo("username", name);
  }
}
