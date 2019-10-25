package rocketchat.spring.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

public class Channel {

  @JsonProperty("_id")
  private String id;

  private String name;

  //todo; add channel type

  private String[] usernames;

  private long msgs;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String[] getUsernames() {
    return usernames;
  }

  public void setUsernames(String[] usernames) {
    this.usernames = usernames;
  }

  public long getMsgs() {
    return msgs;
  }

  public void setMsgs(long msgs) {
    this.msgs = msgs;
  }

  @Override
  public String toString() {
    return "Channel{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", usernames=" + Arrays.toString(usernames) +
        ", msgs=" + msgs +
        '}';
  }
}
