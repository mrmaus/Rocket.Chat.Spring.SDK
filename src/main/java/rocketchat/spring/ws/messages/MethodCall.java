package rocketchat.spring.ws.messages;

/**
 * Parent class for all Method Calls https://rocket.chat/docs/developer-guides/realtime-api/method-calls/
 */
public abstract class MethodCall extends Message implements IdentityAware {

  private String id;
  private String method;

  private Object[] params;

  MethodCall() {
    setMsg("method");
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public Object[] getParams() {
    return params;
  }

  public void setParams(Object[] params) {
    this.params = params;
  }
}
