package rocketchat.spring.rest.messages;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Own user update message https://rocket.chat/docs/developer-guides/rest-api/users/updateownbasicinfo/
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserBasicInfo {

  private String email;

  private String name; //display name

  private String username; //login

  private String currentPassword;

  private String newPassword;

  public UserBasicInfo() {
  }

  public UserBasicInfo(UserBasicInfo copy) {
    setEmail(copy.getEmail());
    setName(copy.getName());
    setUsername(copy.getUsername());
    setNewPassword(copy.getNewPassword());
  }

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

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getCurrentPassword() {
    return currentPassword;
  }

  public void setCurrentPassword(String currentPassword) {
    this.currentPassword = currentPassword;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }
}
