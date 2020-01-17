package rocketchat.spring.rest.messages.reply;

/**
 * Generic REST API response entity
 */
public abstract class Reply<T> {
  private boolean success;

  private String error;
  private String errorType;

  public abstract T getEntity();

  public boolean isSuccess() {
    return success;
  }

  public String getError() {
    return error;
  }

  public String getErrorType() {
    return errorType;
  }

  @Override
  public String toString() {
    return "Reply{" +
        "success=" + success +
        ", error='" + error + '\'' +
        ", errorType='" + errorType + '\'' +
        '}';
  }
}
