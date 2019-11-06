package rocketchat.spring.ws;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import rocketchat.spring.ClientProperties;
import rocketchat.spring.model.Subscription;
import rocketchat.spring.ws.messages.Messages;
import rocketchat.spring.ws.messages.Parsers;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Default {@link RealtimeClient} implementation
 */
public class RealtimeClientImpl extends ReactiveRealtimeClient {

  public RealtimeClientImpl(WebSocketClient webSocketClient,
                            ClientProperties properties,
                            ApplicationEventPublisher eventPublisher,
                            RealtimeExecutorFactory executorFactory) {
    super(webSocketClient, properties, eventPublisher, executorFactory);
  }

  @Override
  public void getSubscriptions(Consumer<Subscription> consumer) {
    send(Messages.joinedRooms(), jsonNode ->
        Parsers.GetSubscriptionsResponseParser.parse(jsonNode).forEach(consumer));
  }

  @Override
  public void sendMessage(String roomId, String message) {
    send(Messages.sendTextMessage(roomId, message));
  }

  @Override
  public void streamRoomMessages(String roomId, Consumer<String> subscriptionId) {
    send(Messages.streamRoomMessages(roomId), json -> {
      if (subscriptionId != null) {
        subscriptionId.accept(json.get("subs").get(0).asText());
      }
    });
  }

  @Override
  public void streamPersonalMessages(Consumer<String> subscriptionId) {
    streamRoomMessages("__my_messages__", subscriptionId);
  }

  @Override
  public void streamSubscriptionChanges() {
    send(Messages.streamNotifyUser(userId(), "subscriptions-changed"));
  }

  @Override
  public void cancelStream(String id) {
    send(Messages.unsubscribe(id));
  }
}
