package rocketchat.spring;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.netty.http.client.HttpClient;
import rocketchat.spring.rest.ReactiveRocketChatClient;
import rocketchat.spring.rest.ReactiveRocketChatClientImpl;
import rocketchat.spring.rest.RocketChatClient;
import rocketchat.spring.rest.RocketChatClientImpl;
import rocketchat.spring.ws.RealtimeClient;
import rocketchat.spring.ws.RealtimeClientImpl;
import rocketchat.spring.ws.SubscriptionsManager;

import javax.net.ssl.SSLException;

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
  public HttpClient httpClient() {
    if (properties.isIgnoreSsl()) {
      try {
        final SslContext sslContext = SslContextBuilder
            .forClient()
            .trustManager(InsecureTrustManagerFactory.INSTANCE)
            .build();
        return HttpClient.create().secure(t -> t.sslContext(sslContext));
      } catch (SSLException e) {
        throw new RuntimeException(e);
      }
    }
    return HttpClient.create();
  }

  @Bean(destroyMethod = "stop")
  @ConditionalOnMissingBean
  public RealtimeClient realtimeClient() {
    final ReactorNettyWebSocketClient webSocketClient = new ReactorNettyWebSocketClient(httpClient());
    return new RealtimeClientImpl(webSocketClient, properties, applicationEventPublisher);
  }

  @Bean
  @ConditionalOnMissingBean
  public ReactiveRocketChatClient reactiveRocketChatClient() {
    return new ReactiveRocketChatClientImpl(httpClient(), properties);
  }

  @Bean
  @ConditionalOnMissingBean
  public RocketChatClient rocketChatClient(ReactiveRocketChatClient reactiveClient) {
    return new RocketChatClientImpl(reactiveClient);
  }

  @Bean
  @ConditionalOnProperty(prefix = ClientProperties.PREFIX, name = "subscription-manager-enabled", matchIfMissing = false)
  public SubscriptionsManager subscriptionsManager(RealtimeClient client) {
    return new SubscriptionsManager(client);
  }
}
