package rocketchat.spring.rest.messages;

import java.util.Arrays;

public class CreateChannel {

  /**
   * The name of the new channel
   */
  private String name;

  /**
   * The users to add to the channel when it is created.
   */
  private String[] members = new String[0];

  /**
   * Set if the channel is read only or not.
   */
  private boolean readOnly;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String[] getMembers() {
    return members;
  }

  public void setMembers(String[] members) {
    this.members = members;
  }

  public boolean isReadOnly() {
    return readOnly;
  }

  public void setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
  }

  @Override
  public String toString() {
    return "CreateChannel{" +
        "name='" + name + '\'' +
        ", members=" + Arrays.toString(members) +
        ", readOnly=" + readOnly +
        '}';
  }
}
