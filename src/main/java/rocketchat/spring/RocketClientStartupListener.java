package rocketchat.spring;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import rocketchat.spring.ws.RealtimeClient;

/**
 * Global listener for RocketChat client for performing some initializations after context was refreshed
 */
public class RocketClientStartupListener implements ApplicationListener<ApplicationStartedEvent> {

  @Override
  public void onApplicationEvent(ApplicationStartedEvent event) {
    final ConfigurableApplicationContext context = event.getApplicationContext();

    final ClientProperties properties = context.getBean(ClientProperties.class);
    if (properties.isAutoStart()) {
      final RealtimeClient client = context.getBean(RealtimeClient.class);
      client.start();
    }
  }
}
