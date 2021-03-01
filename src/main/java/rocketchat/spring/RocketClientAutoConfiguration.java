package rocketchat.spring;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
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
import rocketchat.spring.ws.RealtimeExecutorFactory;

import javax.net.ssl.SSLException;
import java.util.concurrent.Executors;

@Configuration
@EnableConfigurationProperties(ClientProperties.class)
public class RocketClientAutoConfiguration {

  private final ClientProperties properties;
  private final ConfigurableApplicationContext context;

  public RocketClientAutoConfiguration(ClientProperties properties, ConfigurableApplicationContext context) {
    this.properties = properties;
    this.context = context;
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

  @Bean
  @ConditionalOnMissingBean
  public RealtimeExecutorFactory realtimeExecutorFactory() {
    return () -> Executors.newFixedThreadPool(10);
  }

  @Bean(destroyMethod = "stop")
  @ConditionalOnMissingBean
  public RealtimeClient realtimeClient(RealtimeExecutorFactory realtimeExecutorFactory) {
    final ReactorNettyWebSocketClient webSocketClient = new ReactorNettyWebSocketClient(httpClient());
    webSocketClient.setMaxFramePayloadLength(properties.getMaxFrameSize());
    return new RealtimeClientImpl(webSocketClient, properties, context, realtimeExecutorFactory);
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
}
