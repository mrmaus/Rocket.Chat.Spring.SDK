package rocketchat.spring.ws.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

/**
 * Factory class for {@link Message} instances and helpers around that
 */
public class Messages {
  private static final ObjectMapper mapper = new ObjectMapper();

  /**
   * Raw reply for ping request, hard-coding string here since it is always same
   */
  public static String pong() {
    return "{\"msg\":\"pong\"}";
  }

  /**
   * Connect message https://rocket.chat/docs/developer-guides/realtime-api/
   */
  public static Message connect() {
    return new ConnectMessage();
  }

  /**
   * Login method call https://rocket.chat/docs/developer-guides/realtime-api/method-calls/login/
   */
  public static Message login(String username, String password) {
    return new LoginMessage(username, password);
  }

  /**
   * https://rocket.chat/docs/developer-guides/realtime-api/method-calls/get-subscriptions/
   */
  public static Message joinedRooms() {
    return new JoinedRoomsRequest();
  }

  /**
   * Send simple text message into specified room
   */
  public static Message sendTextMessage(String roomId, String message) {
    return new SendMessage(roomId, message);
  }


  /**
   * Request messages stream from the specified room. Once requested (and bot user can access the room), the bot will
   * start receiving all posted messages within the room
   */
  public static Message streamRoomMessages(String roomId) {
    return new StreamRoomMessages(roomId);
  }

  /**
   * Request user notification stream: https://rocket.chat/docs/developer-guides/realtime-api/subscriptions/stream-notify-user/
   */
  public static Message streamNotifyUser(String userId, String eventId) {
    return new StreamNotifyUser(userId, eventId);
  }

  /**
   * Stream 'unsubscribe' message https://rocket.chat/docs/developer-guides/realtime-api/subscriptions/
   */
  public static Message unsubscribe(String streamId) {
    final Unsubscribe unsubscribe = new Unsubscribe();
    unsubscribe.setId(streamId);
    return unsubscribe;
  }

  /**
   * Serializes provided message to JSON as string
   */
  public static String toJsonString(Message message) {
    try {
      return mapper.writeValueAsString(message);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Parses provided string to JSON node
   */
  public static JsonNode parse(String message) {
    try {
      return mapper.readTree(message);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static JsonNode parse(InputStream in) {
    try {
      return mapper.readTree(in);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  /**
   * Initial 'connect' message https://rocket.chat/docs/developer-guides/realtime-api/
   */
  static class ConnectMessage extends Message {
    private String version = "1";
    private String[] support = new String[]{"1"};

    ConnectMessage() {
      setMsg("connect");
    }

    public String getVersion() {
      return version;
    }

    public void setVersion(String version) {
      this.version = version;
    }

    public String[] getSupport() {
      return support;
    }

    public void setSupport(String[] support) {
      this.support = support;
    }
  }

  /**
   * Login message https://rocket.chat/docs/developer-guides/realtime-api/method-calls/login/
   */
  static class LoginMessage extends MethodCall {
    LoginMessage(String username, String password) {
      setMethod("login");
      setParams(new BasicAuth[]{new BasicAuth(username, password)});
    }

    static class BasicAuth {
      private Map<String, String> user;
      private String password;

      private BasicAuth(String username, String password) {
        this.user = Collections.singletonMap("username", username);
        this.password = password;
      }

      public Map<String, String> getUser() {
        return user;
      }

      public String getPassword() {
        return password;
      }
    }
  }

  static class JoinedRoomsRequest extends MethodCall {
    JoinedRoomsRequest() { //todo: add constructor with date
      setMethod("subscriptions/get");
      setParams(new Object[0]);
    }
  }

  /**
   * Room messages stream request https://rocket.chat/docs/developer-guides/realtime-api/subscriptions/stream-room-messages/
   */
  static class StreamRoomMessages extends StreamMessage {
    StreamRoomMessages(String roomId) {
      setName("stream-room-messages");
      setParams(new Object[]{roomId, false});
    }
  }

  static class StreamNotifyUser extends StreamMessage {
    StreamNotifyUser(String userId, String event) {
      setName("stream-notify-user");
      setParams(new Object[]{userId + "/" + event, false});
    }
  }

  /**
   * Stream 'unsubscribe' message https://rocket.chat/docs/developer-guides/realtime-api/subscriptions/
   */
  static class Unsubscribe extends Message {
    private String id;

    Unsubscribe() {
      setMsg("unsub");
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }
  }

}
