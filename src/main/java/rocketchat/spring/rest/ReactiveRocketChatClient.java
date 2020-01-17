package rocketchat.spring.rest;

import reactor.core.publisher.Mono;
import rocketchat.spring.model.IdOrName;
import rocketchat.spring.rest.messages.*;
import rocketchat.spring.rest.messages.reply.RoomInfoReply;

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

  /**
   * Retrieves channel details
   * <p>
   * https://rocket.chat/docs/developer-guides/rest-api/channels/info/
   */
  Mono<ChannelReply> channelInfo(ChannelInfo info);

  /**
   * https://rocket.chat/docs/developer-guides/rest-api/rooms/info/
   */
  Mono<RoomInfoReply> roomInfo(IdOrName token);

  /**
   * Retrieves user details
   * <p>
   * https://rocket.chat/docs/developer-guides/rest-api/users/info/
   */
  Mono<UserReply> userInfo(UserInfo info);

  /**
   * Creates new channel
   * <p>
   * https://rocket.chat/docs/developer-guides/rest-api/channels/create/
   */
  Mono<ChannelReply> createChannel(Mono<CreateChannel> message);

  /**
   * Adds a user to the channel.
   * <p>
   * https://rocket.chat/docs/developer-guides/rest-api/channels/invite/
   */
  Mono<ChannelReply> inviteUserToChannel(String roomId, String userId);

  /**
   * Removes user from channel
   * <p>
   * https://rocket.chat/docs/developer-guides/rest-api/channels/kick/
   */
  Mono<ChannelReply> removeUserFromChannel(String roomId, String userId);

  /**
   * Creates new user
   * <p>
   * https://rocket.chat/docs/developer-guides/rest-api/users/create/
   */
  Mono<UserReply> createUser(Mono<CreateUser> message);

  /**
   * Update own profile and change password
   * <p>
   * https://rocket.chat/docs/developer-guides/rest-api/users/updateownbasicinfo/
   */
  Mono<UserReply> updateOwnBasicInfo(Mono<UserBasicInfo> message);
}
