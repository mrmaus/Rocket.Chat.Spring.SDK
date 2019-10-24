package rocketchat.spring.ws.messages;

/**
 * WebSocket message that requires ID
 */
public interface IdentityAware {

  String getId();

  void setId(String id);
}
