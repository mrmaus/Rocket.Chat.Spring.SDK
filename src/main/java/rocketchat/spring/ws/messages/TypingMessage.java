package rocketchat.spring.ws.messages;

/**
 * https://rocket.chat/docs/developer-guides/realtime-api/method-calls/notify-room-stream/
 */
public class TypingMessage extends MethodCall {

  public TypingMessage(String roomId, String username, boolean typing) {
    setMethod("stream-notify-room");
    setParams(new Object[]{
        roomId + "/typing",
        username,
        typing
    });
  }
}
