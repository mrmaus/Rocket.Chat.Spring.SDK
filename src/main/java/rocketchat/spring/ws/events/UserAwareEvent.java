package rocketchat.spring.ws.events;

/**
 * {@link RocketChatEvent} with user context
 */
public interface UserAwareEvent extends RocketChatEvent {

  User getUser();

  class User {
    private final String login;
    private final String displayName;

    User(String login, String displayName) {
      this.login = login;
      this.displayName = displayName;
    }

    public String getLogin() {
      return login;
    }

    public String getDisplayName() {
      return displayName;
    }

    @Override
    public String toString() {
      return "User{" +
          "login='" + login + '\'' +
          ", displayName='" + displayName + '\'' +
          '}';
    }
  }
}
