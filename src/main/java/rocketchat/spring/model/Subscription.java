package rocketchat.spring.model;

import java.util.Date;

public class Subscription {

  private RoomType roomType;

  /**
   * Timestamp the room was created at
   */
  private Date roomCreatedAt;

  /**
   * Last seen timestamp (The last time the user has seen a message in the room)
   */
  private Date lastSeen;

  private String roomName;

  private String roomId;

  /**
   * Whether the room the subscription is for has been opened or not (defaults to false on direct messages). This is
   * used in the clients to determine whether the user can see this subscription in their list, since you can hide rooms
   * from being visible without leaving them.
   */
  private boolean open;

  /**
   * Whether there is an alert to be displayed to the user
   */
  private boolean alert;

  /**
   * The total of unread messages
   */
  private int unread;

  public RoomType getRoomType() {
    return roomType;
  }

  public void setRoomType(RoomType roomType) {
    this.roomType = roomType;
  }

  public Date getRoomCreatedAt() {
    return roomCreatedAt;
  }

  public void setRoomCreatedAt(Date roomCreatedAt) {
    this.roomCreatedAt = roomCreatedAt;
  }

  public Date getLastSeen() {
    return lastSeen;
  }

  public void setLastSeen(Date lastSeen) {
    this.lastSeen = lastSeen;
  }

  public String getRoomName() {
    return roomName;
  }

  public void setRoomName(String roomName) {
    this.roomName = roomName;
  }

  public String getRoomId() {
    return roomId;
  }

  public void setRoomId(String roomId) {
    this.roomId = roomId;
  }

  public boolean isOpen() {
    return open;
  }

  public void setOpen(boolean open) {
    this.open = open;
  }

  public boolean isAlert() {
    return alert;
  }

  public void setAlert(boolean alert) {
    this.alert = alert;
  }

  public int getUnread() {
    return unread;
  }

  public void setUnread(int unread) {
    this.unread = unread;
  }
}
