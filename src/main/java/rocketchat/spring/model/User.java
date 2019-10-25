package rocketchat.spring.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Date;

public class User {

  @JsonProperty("_id")
  private String id;

  private Date createdAt;

  private String username;

  //todo; emails

  private String type;

  private String status;

  private boolean active;

  private String[] roles;

  private String name;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "User{" +
        "id='" + id + '\'' +
        ", createdAt=" + createdAt +
        ", username='" + username + '\'' +
        ", type='" + type + '\'' +
        ", status='" + status + '\'' +
        ", active=" + active +
        ", roles=" + Arrays.toString(roles) +
        ", name='" + name + '\'' +
        '}';
  }
}
