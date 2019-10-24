package rocketchat.spring.rest.messages;

/**
 * Username-password based login request
 * <p>
 * https://rocket.chat/docs/developer-guides/rest-api/authentication/login/
 */
public class Login {

  private String user;
  private String password;

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Login response
   */
  public static class Reply {
    private String status;
    private Data data;

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }

    public Data getData() {
      return data;
    }

    public void setData(Data data) {
      this.data = data;
    }

    public static class Data {
      private String authToken;
      private String userId;

      public String getAuthToken() {
        return authToken;
      }

      public void setAuthToken(String authToken) {
        this.authToken = authToken;
      }

      public String getUserId() {
        return userId;
      }

      public void setUserId(String userId) {
        this.userId = userId;
      }
    }
  }
}
