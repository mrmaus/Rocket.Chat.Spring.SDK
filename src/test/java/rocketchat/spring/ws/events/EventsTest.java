package rocketchat.spring.ws.events;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import rocketchat.spring.ws.messages.Messages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class EventsTest {

  @Test
  public void test_parse_stream_room_message_success() {
    final JsonNode json = Messages.parse(getClass().getResourceAsStream("message1.json"));

    final MessageEvent event = (MessageEvent) Events.parse(json);
    assertNotNull(event);
    assertEquals("GENERAL", event.getRoomId());
    assertEquals("test2", event.getMessage());

    final MessageEvent.User user = event.getUser();
    assertNotNull(user);
    assertEquals("admin", user.getLogin());
  }
}