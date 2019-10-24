package rocketchat.spring.ws.messages;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.function.Function;

/**
 * WebSocket raw message parser
 */
public interface MessageParser<T> extends Function<JsonNode, T> {

  @Override
  default T apply(JsonNode jsonNode) {
    return parse(jsonNode);
  }

  /**
   * Parses provided raw json into message object
   */
  T parse(JsonNode node);

}
