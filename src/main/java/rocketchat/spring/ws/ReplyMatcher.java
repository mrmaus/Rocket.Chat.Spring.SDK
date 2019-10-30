package rocketchat.spring.ws;

import com.fasterxml.jackson.databind.JsonNode;
import rocketchat.spring.ws.messages.IdentityAware;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Attempts to match outbound and inbound messages for request-response type of flow
 */
class ReplyMatcher {

  //todo: limit the size of the map to prevent memory leak in case of failed responses! todo: better implementation!
  private final Map<String, Consumer<JsonNode>> replyHandlers = new ConcurrentHashMap<>();

  void add(IdentityAware request, Consumer<JsonNode> replyHandler) {
    add(request.getId(), replyHandler);
  }

  void add(String id, Consumer<JsonNode> replyHandler) {
    if (replyHandler != null && id != null) {
      this.replyHandlers.put(id, replyHandler);
    }
  }

  Optional<Consumer<JsonNode>> match(JsonNode json) {
    if (replyHandlers.isEmpty() || json == null) {
      return Optional.empty();
    }
    final String id = json.hasNonNull("id") ? json.get("id").asText() : null;
    if (id != null) {
      final Consumer<JsonNode> handler = replyHandlers.remove(id);
      if (handler != null) {
        return Optional.of(handler);
      }
    }

    // matching subscription response by subs array
    if (isSubsReply(json)) {
      for (JsonNode sub : json.get("subs")) {
        final Consumer<JsonNode> handler = replyHandlers.remove(sub.asText());
        if (handler != null) {
          return Optional.of(handler);
        }
      }
    }

    // matching initial connect message reply, unfortunately no better matching way available atm
    if (isConnectReply(json)) {
      return Optional.ofNullable(replyHandlers.remove("connect"));
    }

    return Optional.empty();
  }

  private boolean isSubsReply(JsonNode json) {
    final String msg = JsonUtils.getMsg(json);
    return "ready".equals(msg) && json.hasNonNull("subs");
  }

  private boolean isConnectReply(JsonNode json) { //anyone knows better way to match connect reply...?
    return json.size() == 1 && "0".equals(JsonUtils.getText(json, "server_id"));
  }
}
