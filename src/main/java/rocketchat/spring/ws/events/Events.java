package rocketchat.spring.ws.events;

import com.fasterxml.jackson.databind.JsonNode;
import rocketchat.spring.model.RoomType;
import rocketchat.spring.ws.JsonUtils;

import static rocketchat.spring.ws.JsonUtils.getText;

/**
 * Bridges RocketChat WebSocket JSON responses and {@link RocketChatEvent} instances
 */
public class Events {

  /**
   * Attempts to create corresponding {@link RocketChatEvent} instance for the provided payload. Might return NULL
   */
  public static RocketChatEvent parse(JsonNode json) {
    final String collection = getText(json, "collection");

    if (collection != null) {
      switch (collection) {
        case "stream-room-messages": {
          final JsonNode argsNode = JsonUtils.navigate(json, "fields", "args");

          if (argsNode.size() > 0) {
            final JsonNode msgArgs = argsNode.get(0);

            final MessageEvent.Builder builder = new MessageEvent.Builder()
                .roomId(msgArgs.get("rid").asText())
                .message(msgArgs.get("msg").asText())
                .user(new MessageEvent.User(
                    getText(msgArgs.get("u"), "username"),
                    getText(msgArgs.get("u"), "name")))
                .messageType(MessageEvent.MessageType.parse(getText(msgArgs, "t")));

            if (msgArgs.hasNonNull("mentions")) {
              msgArgs.get("mentions").forEach(n -> {
                builder.addMention(new UserAwareEvent.User(getText(n, "username"), getText(n, "name")));
              });
            }

            if (argsNode.size() > 1) {
              final JsonNode roomArgs = argsNode.get(1);
              builder
                  .roomParticipant(roomArgs.get("roomParticipant").booleanValue())
                  .roomType(RoomType.parse(getText(roomArgs, "roomType")))
                  .roomName(getText(roomArgs, "roomName"));
            }
            return builder.build();
          }
        }

        case "stream-notify-user": {
          final JsonNode args = JsonUtils.navigate(json, "fields", "args");

          final String eventName = getText(json.get("fields"), "eventName");
          if (eventName.endsWith("/subscriptions-changed")) {
            final String type = args.get(0).asText();
            final String roomId = getText(args.get(1), "rid");

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
