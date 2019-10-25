package rocketchat.spring.rest.messages;

import rocketchat.spring.model.User;

public class UserReply {
  private boolean success;

  private User user;

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public String toString() {
    return "Reply{" +
        "success=" + success +
        ", user=" + user +
        '}';
  }
}
