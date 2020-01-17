package rocketchat.spring.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import rocketchat.spring.model.RoomType;

import java.io.IOException;

public class RoomTypeDeserializer extends StdDeserializer<RoomType> {

  public RoomTypeDeserializer() {
    super(RoomType.class);
  }

  @Override
  public RoomType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    return RoomType.parse(p.getText());
  }
}
