package rocketchat.spring.rest.messages;

import rocketchat.spring.model.Channel;

public class ChannelReply {
  private boolean success;
  private Channel channel;

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public Channel getChannel() {
    return channel;
  }

  public void setChannel(Channel channel) {
    this.channel = channel;
  }

  @Override
  public String toString() {
    return "Reply{" +
        "success=" + success +
        ", channel=" + channel +
        '}';
  }
}
