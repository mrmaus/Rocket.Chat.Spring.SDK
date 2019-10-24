package rocketchat.spring.ws.messages;

/**
 * Parent class for all stream requests https://rocket.chat/docs/developer-guides/realtime-api/subscriptions/
 */
public abstract class StreamMessage extends Message implements IdentityAware {

  private String id;
  private String name;
  private Object[] params;

  StreamMessage() {
    setMsg("sub");
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Object[] getParams() {
    return params;
  }

  public void setParams(Object[] params) {
    this.params = params;
  }
}
