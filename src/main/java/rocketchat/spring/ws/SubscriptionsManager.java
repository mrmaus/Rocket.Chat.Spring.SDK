package rocketchat.spring.ws;

import org.springframework.context.event.EventListener;
import rocketchat.spring.ws.events.ClientStartedEvent;
import rocketchat.spring.ws.events.SubscriptionAddedEvent;
import rocketchat.spring.ws.events.SubscriptionRemovedEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Automatically manages all subscriptions, subscribing to all active message streams and un-subscribing automatically
 *
 * @deprecated looks like just streaming personal messages would do the job
 */
@Deprecated
public class SubscriptionsManager {

  private final RealtimeClient client;

  private final Map<String, String> roomToSubscription = new ConcurrentHashMap<>();

  public SubscriptionsManager(RealtimeClient client) {
    this.client = client;
  }

  @EventListener
  public void onStart(ClientStartedEvent event) {
    client.streamSubscriptionChanges(); //listen for all changes in subscriptions

    /* subscribe to message streams for all existing subscriptions */
    client.getSubscriptions(subscription -> {
      final String roomId = subscription.getRoomId();
      client.streamRoomMessages(roomId, subscriptionId -> add(roomId, subscriptionId));
    });
  }

  @EventListener
  public void onSubscriptionAdded(SubscriptionAddedEvent event) {
    final String roomId = event.getRoomId();
    client.streamRoomMessages(roomId, subscriptionId -> add(roomId, subscriptionId));
  }

  @EventListener
  public void onSubscriptionRemoved(SubscriptionRemovedEvent event) {
    final String streamId = roomToSubscription.remove(event.getRoomId());
    if (streamId != null) {
      client.cancelStream(streamId);
    }
  }

  //todo: rework the implementation, doesn't look thread-safe for now
  //      timestamp entries and implement cleanup to avoid potential memory leak
  private void add(String roomId, String subscriptionId) {
    final String existing = roomToSubscription.put(roomId, subscriptionId);
    if (existing != null) {
      client.cancelStream(existing); //cancel existing subscription to avoid duplicated messages
    }
  }
}
