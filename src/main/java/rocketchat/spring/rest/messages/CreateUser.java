package rocketchat.spring.rest.messages;

import rocketchat.spring.model.User;

import java.util.Arrays;

public class CreateUser {

  /**
   * The email address for the user.
   */
  private String email;

  /**
   * The display name of the user.
   */
  private String name;

  /**
   * The password for the user.
   */
  private String password;

  /**
   * The username for the user.
   */
  private String username;

  /**
   * Whether the user is active, which determines if they can login or not.
   */
  private boolean active = true;

  /**
   * The roles the user has assigned to them on creation.
   */
  private String[] roles = new String[]{"user"};

  /**
   * Whether the user should join the default channels when created.
   */
  private boolean joinDefaultChannels = true;

  /**
   * Should the user be required to change their password when they login?
   */
  private boolean requirePasswordChange = false;

  /**
   * Should the user get a welcome email?
   */
  private boolean sendWelcomeEmail = false;

  /**
   * Should the user’s email address be verified when created?
   */
  private boolean verified = false;

  //todo: add customFields


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public String[] getRoles() {
    return roles;
  }

  public void setRoles(String[] roles) {
    this.roles = roles;
  }

  public boolean isJoinDefaultChannels() {
    return joinDefaultChannels;
  }

  public void setJoinDefaultChannels(boolean joinDefaultChannels) {
    this.joinDefaultChannels = joinDefaultChannels;
  }

  public boolean isRequirePasswordChange() {
    return requirePasswordChange;
  }

  public void setRequirePasswordChange(boolean requirePasswordChange) {
    this.requirePasswordChange = requirePasswordChange;
  }

  public boolean isSendWelcomeEmail() {
    return sendWelcomeEmail;
  }

  public void setSendWelcomeEmail(boolean sendWelcomeEmail) {
    this.sendWelcomeEmail = sendWelcomeEmail;
  }

  public boolean isVerified() {
    return verified;
  }

  public void setVerified(boolean verified) {
    this.verified = verified;
  }

  @Override
  public String toString() {
    return "CreateUser{" +
        "email='" + email + '\'' +
        ", name='" + name + '\'' +
        ", password='" + password + '\'' +
        ", username='" + username + '\'' +
        ", active=" + active +
        ", roles=" + Arrays.toString(roles) +
        ", joinDefaultChannels=" + joinDefaultChannels +
        ", requirePasswordChange=" + requirePasswordChange +
        ", sendWelcomeEmail=" + sendWelcomeEmail +
        ", verified=" + verified +
        '}';
  }

}
