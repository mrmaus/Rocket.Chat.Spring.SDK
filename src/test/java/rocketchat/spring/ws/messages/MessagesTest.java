package rocketchat.spring.ws.messages;


import org.junit.Ignore;
import org.junit.Test;

@Ignore
class MessagesTest {

  @Test
  void test_LoginMessage_serialization() {
    final Message login = Messages.login("hello", "world");
    System.out.println(Messages.toJsonString(login));
// todo: assert
  }
}