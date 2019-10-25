package rocketchat.spring.rest.messages;

abstract class FieldQuery {

  private final String fieldName;
  private final String fieldValue;

  FieldQuery(String fieldName, String fieldValue) {
    this.fieldName = fieldName;
    this.fieldValue = fieldValue;
  }

  public String getFieldName() {
    return fieldName;
  }

  public String getFieldValue() {
    return fieldValue;
  }
}
