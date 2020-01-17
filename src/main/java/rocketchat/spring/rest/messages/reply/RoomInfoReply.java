package rocketchat.spring.rest.messages.reply;

import rocketchat.spring.model.Room;

public class RoomInfoReply extends Reply<Room> {

  private Room room;

  @Override
  public Room getEntity() {
    return getRoom();
  }

  public Room getRoom() {
    return room;
  }

  public void setRoom(Room room) {
    this.room = room;
  }

  @Override
  public String toString() {
    return "RoomInfoReply{" +
        "room=" + room +
        "} " + super.toString();
  }
}
