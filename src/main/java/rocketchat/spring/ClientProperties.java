package rocketchat.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Configuration parameters for REST and realtime clients
 */
@ConfigurationProperties(prefix = "rocketchat")
public class ClientProperties {

  /**
   * The RocketChat server base url (ex: http://localhost:3000)
   */
  private String baseUrl;

  /**
   * The bot user name
   */
  private String user;

  /**
   * The bot password
   */
  private String password;

  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

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

  public URI webSocketUri() {
    try {
      return new URI(StringUtils.applyRelativePath(baseUrl + "/", "/websocket"));
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}
