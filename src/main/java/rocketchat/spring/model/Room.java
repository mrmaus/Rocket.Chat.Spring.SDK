package rocketchat.spring.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import rocketchat.spring.json.RoomTypeDeserializer;

public class Room {

  @JsonProperty("_id")
  private String id;

  private String name;

  @JsonProperty("t")
  @JsonDeserialize(using = RoomTypeDeserializer.class, as = RoomType.class)
  private RoomType type;

  private int usersCount;

  @JsonProperty("msgs")
  private int messagesCount;

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

  public RoomType getType() {
    return type;
  }

  public void setType(RoomType type) {
    this.type = type;
  }

  public int getUsersCount() {
    return usersCount;
  }

  public void setUsersCount(int usersCount) {
    this.usersCount = usersCount;
  }

  public int getMessagesCount() {
    return messagesCount;
  }

  public void setMessagesCount(int messagesCount) {
    this.messagesCount = messagesCount;
  }
}
