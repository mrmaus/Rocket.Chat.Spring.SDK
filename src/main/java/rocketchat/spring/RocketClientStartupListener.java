package rocketchat.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import rocketchat.spring.ws.RealtimeClient;

/**
 * Global listener for RocketChat client for performing some initializations after context was refreshed
 */
public class RocketClientStartupListener implements ApplicationListener<ApplicationReadyEvent> {
  private static final Logger log = LoggerFactory.getLogger(RocketClientStartupListener.class);

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    final ConfigurableApplicationContext context = event.getApplicationContext();

    final ClientProperties properties = context.getBeanProvider(ClientProperties.class).getIfAvailable();
    if (properties == null) {
      log.error("Automatic application startup failed");
      return;
    }

    if (properties.isAutoStart()) {
      final RealtimeClient client = context.getBean(RealtimeClient.class);
      client.start();
    }
  }
}
