package rocketchat.spring.ws.messages;

import java.util.HashMap;

class SendMessage extends MethodCall {

  SendMessage(String roomId, String message) {
    final HashMap<Object, Object> p = new HashMap<>();
    p.put("rid", roomId);
    p.put("msg", message);

    setMethod("sendMessage");
    setParams(new Object[]{p});
  }
}
