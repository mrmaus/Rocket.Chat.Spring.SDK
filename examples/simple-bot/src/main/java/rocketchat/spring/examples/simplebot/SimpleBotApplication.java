package rocketchat.spring.examples.simplebot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import rocketchat.spring.ws.RealtimeClient;

@SpringBootApplication
public class SimpleBotApplication {

  public static void main(String[] args) {
    SpringApplication.run(SimpleBotApplication.class, args);
  }

  @Bean
  public CommandLineRunner starter(RealtimeClient client) {
    return args -> client.start();
  }

}
