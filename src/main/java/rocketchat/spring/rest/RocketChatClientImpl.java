package rocketchat.spring.rest;

import reactor.core.publisher.Mono;
import rocketchat.spring.rest.messages.PostMessage;

/**
 * The default implementation of {@link RocketChatClient} is merely a wrapper around reactive client with blocking
 * calls
 */
public class RocketChatClientImpl implements RocketChatClient {

  private final ReactiveRocketChatClient reactiveClient;

  public RocketChatClientImpl(ReactiveRocketChatClient reactiveClient) {
    this.reactiveClient = reactiveClient;
  }

  @Override
  public PostMessage.Reply sendMessage(PostMessage message) {
    return reactiveClient.postMessage(Mono.just(message)).block();
  }
}
