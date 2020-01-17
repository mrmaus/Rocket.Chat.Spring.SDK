package rocketchat.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Configuration parameters for REST and realtime clients
 */
@ConfigurationProperties(prefix = ClientProperties.PREFIX)
public class ClientProperties {

  static final String PREFIX = "rocketchat";

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
   * If set to true, all SSL warnings will be ignored (such as invalid or self-signed certificate)
   */
  private boolean ignoreSsl = false;

  /**
   * Enables automatic management of subscriptions: bot will automatically subscribe to messages stream on each joined
   * channel and unsubscribes accordingly
   *
   * @deprecated
   */
  private boolean subscriptionManagerEnabled = false;

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

  public boolean isIgnoreSsl() {
    return ignoreSsl;
  }

  public void setIgnoreSsl(boolean ignoreSsl) {
    this.ignoreSsl = ignoreSsl;
  }

  public boolean isSubscriptionManagerEnabled() {
    return subscriptionManagerEnabled;
  }

  public void setSubscriptionManagerEnabled(boolean subscriptionManagerEnabled) {
    this.subscriptionManagerEnabled = subscriptionManagerEnabled;
  }

  public URI webSocketUri() {
    try {
      return new URI(StringUtils.applyRelativePath(baseUrl + "/", "/websocket"));
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}
