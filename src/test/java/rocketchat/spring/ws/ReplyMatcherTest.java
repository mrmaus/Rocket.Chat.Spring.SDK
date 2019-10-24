package rocketchat.spring.ws;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import rocketchat.spring.ws.messages.IdentityAware;
import rocketchat.spring.ws.messages.Messages;

import java.util.Optional;
import java.util.function.Consumer;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ReplyMatcherTest {

  private final ReplyMatcher matcher = new ReplyMatcher();

  @Test
  public void test_match_stream_subscription_request() {
    final IdentityAware message = (IdentityAware) Messages.streamRoomMessages("GENERAL");
    message.setId("17");

    final Consumer<JsonNode> consumer = node -> {
    };

    matcher.add(message, consumer);

    final JsonNode response = Messages.parse("{\"msg\":\"ready\",\"subs\":[\"17\"]}");
    final Optional<Consumer<JsonNode>> match = matcher.match(response);
    assertTrue(match.isPresent());
    assertSame(consumer, match.get());
  }

}