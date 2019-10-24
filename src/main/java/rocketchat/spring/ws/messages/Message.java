package rocketchat.spring.ws.messages;

/**
 * Base class for WebSocket messages
 */
public abstract class Message {

  private String msg;

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }
}
