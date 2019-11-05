package rocketchat.spring.rest;

import rocketchat.spring.rest.messages.*;

/**
 * RocketChat REST API Client with blocking api
 */
public interface RocketChatClient {

  /**
   * Posts message
   * <p>
   * https://rocket.chat/docs/developer-guides/rest-api/chat/postmessage
   */
  PostMessage.Reply postMessage(PostMessage message);

  /**
   * Retrieves channel details
   * <p>
   * https://rocket.chat/docs/developer-guides/rest-api/channels/info/
   */
  ChannelReply channelInfo(ChannelInfo info);

  /**
   * Retrieves user details
   * <p>
   * https://rocket.chat/docs/developer-guides/rest-api/users/info/
   */
  UserReply userInfo(UserInfo info);

  /**
   * Creates new channel
   * <p>
   * https://rocket.chat/docs/developer-guides/rest-api/channels/create/
   */
  ChannelReply createChannel(CreateChannel message);

  /**
   * Adds a user to the channel.
   * <p>
   * https://rocket.chat/docs/developer-guides/rest-api/channels/invite/
   */
  ChannelReply inviteUserToChannel(String roomId, String userId);

  /**
   * Removes user from channel
   * <p>
   * https://rocket.chat/docs/developer-guides/rest-api/channels/kick/
   */
  ChannelReply removeUserFromChannel(String roomId, String userId);

  /**
   * Creates new user
   * <p>
   * https://rocket.chat/docs/developer-guides/rest-api/users/create/
   */
  UserReply createUser(CreateUser message);

  /**
   * Update own profile and change password
   * <p>
   * https://rocket.chat/docs/developer-guides/rest-api/users/updateownbasicinfo/
   */
  UserReply updateOwnBasicInfo(UserBasicInfo message);
}
