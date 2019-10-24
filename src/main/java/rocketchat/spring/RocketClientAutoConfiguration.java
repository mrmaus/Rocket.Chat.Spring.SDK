package rocketchat.spring;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import rocketchat.spring.rest.ReactiveRocketChatClient;
import rocketchat.spring.rest.ReactiveRocketChatClientImpl;
import rocketchat.spring.rest.RocketChatClient;
import rocketchat.spring.rest.RocketChatClientImpl;
import rocketchat.spring.ws.RealtimeClient;
import rocketchat.spring.ws.RealtimeClientImpl;

@Configuration
@EnableConfigurationProperties(ClientProperties.class)
public class RocketClientAutoConfiguration {

  private final ClientProperties properties;
  private final ApplicationEventPublisher applicationEventPublisher;

  public RocketClientAutoConfiguration(ClientProperties properties,
                                       ApplicationEventPublisher applicationEventPublisher) {
    this.properties = properties;
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @Bean
  @ConditionalOnMissingBean
  public RealtimeClient realtimeClient() {
    final ReactorNettyWebSocketClient webSocketClient = new ReactorNettyWebSocketClient();
    return new RealtimeClientImpl(webSocketClient, properties, applicationEventPublisher);
  }

  @Bean
  @ConditionalOnMissingBean
  public ReactiveRocketChatClient reactiveRocketChatClient() {
    return new ReactiveRocketChatClientImpl(properties);
  }

  @Bean
  @ConditionalOnMissingBean
  public RocketChatClient rocketChatClient(ReactiveRocketChatClient reactiveClient) {
    return new RocketChatClientImpl(reactiveClient);
  }
}
