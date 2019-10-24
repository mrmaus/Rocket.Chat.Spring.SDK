package rocketchat.spring.ws.events;

import com.fasterxml.jackson.databind.JsonNode;
import rocketchat.spring.ws.JsonUtils;

/**
 * Bridges RocketChat WebSocket JSON responses and {@link RocketChatEvent} instances
 */
public class Events {

  /**
   * Attempts to create corresponding {@link RocketChatEvent} instance for the provided payload. Might return NULL
   */
  public static RocketChatEvent parse(JsonNode json) {
    final String msg = JsonUtils.getMsg(json);
    final String collection = JsonUtils.getText(json, "collection");

    if (collection != null) {
      switch (collection) {
        case "stream-room-messages": {
          final JsonNode args = json.get("fields").get("args").get(0); //todo: more than one arg possible?
          return new MessageEvent(
              args.get("rid").asText(),
              args.get("msg").asText(),
              new MessageEvent.User(
                  JsonUtils.getText(args.get("u"), "username"),
                  JsonUtils.getText(args.get("u"), "name")));
        }

        case "stream-notify-user": {
          final JsonNode args = json.get("fields").get("args");

          final String eventName = JsonUtils.getText(json.get("fields"), "eventName");
          if (eventName.endsWith("/subscriptions-changed")) {
            final String type = args.get(0).asText();
            final String roomId = JsonUtils.getText(args.get(1), "rid");

            switch (type) {
              case "changed":
              case "inserted":
                return new SubscriptionAddedEvent(roomId);
              case "removed":
                return new SubscriptionRemovedEvent(roomId);
            }
          }
        }
      }
    }
    return null;
  }
}
