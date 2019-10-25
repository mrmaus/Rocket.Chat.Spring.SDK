package rocketchat.spring.examples.simplebot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import rocketchat.spring.ws.RealtimeClient;
import rocketchat.spring.ws.SubscriptionsManager;

@SpringBootApplication
public class SimpleBotApplication {

  public static void main(String[] args) {
    SpringApplication.run(SimpleBotApplication.class, args);
  }

  /**
   * Automatically join and release subscription streams
   */
  @Bean
  public SubscriptionsManager subscriptionsManager(RealtimeClient client) {
    return new SubscriptionsManager(client);
  }

  @Bean
  public CommandLineRunner clientStarter(RealtimeClient client) {
    return args -> client.start();
  }

}
