package rocketchat.spring.examples.simplebot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import rocketchat.spring.ws.RealtimeClient;
import rocketchat.spring.ws.events.ClientStartedEvent;

@SpringBootApplication
public class SimpleBotApplication {

  private final RealtimeClient client;

  public SimpleBotApplication(RealtimeClient client) {
    this.client = client;
  }

  public static void main(String[] args) {
    SpringApplication.run(SimpleBotApplication.class, args);
  }

  @Bean
  public CommandLineRunner starter(RealtimeClient client) {
    return args -> client.start();
  }

  @EventListener
  public void onStart(ClientStartedEvent event) {
    client.streamMessages(null);
  }

}
