package rocketchat.spring.rest;

import rocketchat.spring.rest.messages.PostMessage;

/**
 * RocketChat REST API Client with blocking api
 */
public interface RocketChatClient {

  /**
   * Posts message
   * <p>
   * https://rocket.chat/docs/developer-guides/rest-api/chat/postmessage
   */
  PostMessage.Reply sendMessage(PostMessage message);
}
