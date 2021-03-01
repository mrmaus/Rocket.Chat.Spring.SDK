package rocketchat.spring.ws.events;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import rocketchat.spring.model.RoomType;
import rocketchat.spring.ws.messages.Messages;

import static org.junit.Assert.*;


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

  @Test
  public void test_parse_stream_room_message_success_v_3_9_0() {
    final JsonNode json = Messages.parse(getClass().getResourceAsStream("message2.json"));

    final MessageEvent event = (MessageEvent) Events.parse(json);
    assertNotNull(event);
    assertEquals("2NuH67xdSSrAE6eKumhDGoZxTDMaYJ23p5", event.getRoomId());
    assertEquals("hi", event.getMessage());

    final MessageEvent.User user = event.getUser();
    assertNotNull(user);
    assertEquals("admin", user.getLogin());

    assertTrue(event.isRoomParticipant());
    assertSame(RoomType.DIRECT_MESSAGE, event.getRoomType());
  }
}
