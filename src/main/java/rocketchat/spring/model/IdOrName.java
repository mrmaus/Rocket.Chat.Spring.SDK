package rocketchat.spring.model;

/**
 * Contains ID or name value; some Rocket APIs take ID or Name as parameter, this is convenience class to reduce the
 * number of method overloads
 */
public class IdOrName {

  private final String id;
  private final String name;

  private IdOrName(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public static IdOrName id(String id) {
    return new IdOrName(id, null);
  }

  public static IdOrName name(String name) {
    return new IdOrName(null, name);
  }

  public boolean isId() {
    return id != null;
  }

  public String getValue() {
    return isId() ? id : name;
  }
}
