package rocketchat.spring.rest;

import reactor.core.publisher.Mono;
import rocketchat.spring.rest.messages.PostMessage;

/**
 * RocketChat REST API client based on reactive api
 */
public interface ReactiveRocketChatClient {

  /**
   * Posts message
   * <p>
   * https://rocket.chat/docs/developer-guides/rest-api/chat/postmessage
   */
  Mono<PostMessage.Reply> postMessage(Mono<PostMessage> message);
}
