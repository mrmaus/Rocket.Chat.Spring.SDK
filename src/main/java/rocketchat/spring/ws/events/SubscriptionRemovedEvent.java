package rocketchat.spring.ws.events;

/**
 * Fired when user is removed subscription (for example when user was removed from a chatroom)
 */
public class SubscriptionRemovedEvent implements RocketChatEvent {
  private final String roomId;

  public SubscriptionRemovedEvent(String roomId) {
    this.roomId = roomId;
  }

  public String getRoomId() {
    return roomId;
  }
}
