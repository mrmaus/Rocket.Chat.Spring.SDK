package rocketchat.spring.ws.messages;

import rocketchat.spring.ws.MessageObject;

public class SendMessage extends MethodCall {

  public SendMessage(MessageObject messageObject) {
    setMethod("sendMessage");
    setParams(new Object[]{messageObject});
  }
}
