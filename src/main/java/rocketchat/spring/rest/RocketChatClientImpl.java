package rocketchat.spring.rest;

import reactor.core.publisher.Mono;
import rocketchat.spring.rest.messages.*;

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
  public PostMessage.Reply postMessage(PostMessage message) {
    return reactiveClient.postMessage(Mono.just(message)).block();
  }

  @Override
  public ChannelReply channelInfo(ChannelInfo info) {
    return reactiveClient.channelInfo(info).block();
  }

  @Override
  public ChannelReply createChannel(CreateChannel message) {
    return reactiveClient.createChannel(Mono.just(message)).block();
  }

  @Override
  public UserReply userInfo(UserInfo info) {
    return reactiveClient.userInfo(info).block();
  }

  @Override
  public ChannelReply inviteUserToChannel(String roomId, String userId) {
    return reactiveClient.inviteUserToChannel(roomId, userId).block();
  }

  @Override
  public ChannelReply removeUserFromChannel(String roomId, String userId) {
    return reactiveClient.removeUserFromChannel(roomId, userId).block();
  }

  @Override
  public UserReply createUser(CreateUser message) {
    return reactiveClient.createUser(Mono.just(message)).block();
  }
}
