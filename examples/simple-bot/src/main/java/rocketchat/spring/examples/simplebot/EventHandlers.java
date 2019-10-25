package rocketchat.spring.examples.simplebot;

import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import rocketchat.spring.ws.RealtimeClient;
import rocketchat.spring.ws.events.MessageEvent;

@Component
public class EventHandlers {
  private final RealtimeClient client;

  public EventHandlers(RealtimeClient client) {
    this.client = client;
  }

  @EventListener(classes = ContextStartedEvent.class)
  public void startClient() {
    this.client.start();
  }

  /**
   * All messages from all subscribed channels will arrive here
   */
  @EventListener(classes = MessageEvent.class)
  public void onMessage(MessageEvent event) {
    client.sendMessage(event.getRoomId(), "Hi " + event.getUser().getLogin() + ", this is Sample Bot!");
  }
}
