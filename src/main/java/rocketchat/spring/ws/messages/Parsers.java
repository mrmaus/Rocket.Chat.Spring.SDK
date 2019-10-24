package rocketchat.spring.ws.messages;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import rocketchat.spring.model.Subscription;

import java.util.ArrayList;
import java.util.List;

public class Parsers {

  /**
   * Parser for subscription response message
   */
  public static final MessageParser<List<Subscription>> GetSubscriptionsResponseParser = node -> {
    final ArrayNode resultNode = (ArrayNode) node.get("result");

    final ArrayList<Subscription> result = new ArrayList<>(resultNode.size());

    for (JsonNode n : resultNode) {
      final Subscription subscription = new Subscription();
      subscription.setRoomName(n.get("name").asText());
      subscription.setRoomId(n.get("rid").asText());
      result.add(subscription);
    }

    return result;
  };

}
