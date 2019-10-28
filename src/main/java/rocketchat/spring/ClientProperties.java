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

  /**
   * automatically start Realtime API connector on application startup
   */
  private boolean autoStart = true;

  /**
   * If set to true, all SSL warnings will be ignored (such as invalid or self-signed certificate)
   */
  private boolean ignoreSsl = false;

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

  public boolean isAutoStart() {
    return autoStart;
  }

  public void setAutoStart(boolean autoStart) {
    this.autoStart = autoStart;
  }

  public boolean isIgnoreSsl() {
    return ignoreSsl;
  }

  public void setIgnoreSsl(boolean ignoreSsl) {
    this.ignoreSsl = ignoreSsl;
  }

  public URI webSocketUri() {
    try {
      return new URI(StringUtils.applyRelativePath(baseUrl + "/", "/websocket"));
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}
