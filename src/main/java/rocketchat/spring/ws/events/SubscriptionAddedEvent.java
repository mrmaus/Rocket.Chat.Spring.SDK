package rocketchat.spring.ws.events;

/**
 * Fired when user received a new subscription (for example when user was invited to a chatroom)
 */
public class SubscriptionAddedEvent implements RocketChatEvent {
  private final String roomId;

  public SubscriptionAddedEvent(String roomId) {
    this.roomId = roomId;
  }

  public String getRoomId() {
    return roomId;
  }
}
