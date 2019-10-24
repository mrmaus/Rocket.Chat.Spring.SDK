package rocketchat.spring.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoProcessor;
import rocketchat.spring.ClientProperties;
import rocketchat.spring.rest.messages.Login;
import rocketchat.spring.rest.messages.PostMessage;

public class ReactiveRocketChatClientImpl implements ReactiveRocketChatClient {
  private static final Logger log = LoggerFactory.getLogger(ReactiveRocketChatClientImpl.class);

  //todo: default content type
  //todo: add response logger

  private final WebClient webClient;
  private final Login login;

  /**
   * Initializer for security context: the context contains authentication data required for certain REST calls
   */
  private volatile Mono<SecurityContext> securityContext;

  public ReactiveRocketChatClientImpl(ClientProperties properties) {
    this.webClient = WebClient.builder()
        .baseUrl(properties.getBaseUrl())
        .filter(logRequest())
        .build();
    this.securityContext = resetSecurityContext();

    final Login login = new Login();
    login.setUser(properties.getUser());
    login.setPassword(properties.getPassword());
    this.login = login;
  }

  private Mono<SecurityContext> resetSecurityContext() {
    return securityContext = MonoProcessor.from(
        Mono.fromCallable(() -> login)
            .flatMap(login -> login(Mono.just(login)))
            .map(reply -> new SecurityContext(
                reply.getData().getUserId(),
                reply.getData().getAuthToken())
            )
    );
  }

  @Override
  public Mono<PostMessage.Reply> postMessage(Mono<PostMessage> message) {
    return doSecurePost("/api/v1/chat.postMessage", message, PostMessage.class, PostMessage.Reply.class);
  }

  private <REQ, RES> Mono<RES> doSecurePost(String uri, Mono<REQ> body, Class<REQ> requestClass, Class<RES> replyClass) {
    return
        securityContext
            .flatMap(context -> withSecurityHeaders(buildPostRequest(uri, body, requestClass), context)
                .exchange()
                .flatMap(response -> {
                  if (response.statusCode() == HttpStatus.UNAUTHORIZED) {
                    return resetSecurityContext()
                        .flatMap(context2 -> withSecurityHeaders(buildPostRequest(uri, body, requestClass), context2)
                            .retrieve().bodyToMono(replyClass));
                  }
                  //todo: handle other response codes
                  return response.bodyToMono(replyClass);
                })
            );
  }

  private <REQ> WebClient.RequestHeadersSpec<?> buildPostRequest(String uri,
                                                                 Mono<REQ> body,
                                                                 Class<REQ> requestClass) {
    return webClient.post()
        .uri(uri)
        .contentType(MediaType.APPLICATION_JSON)
        .body(body, requestClass);
  }

  private WebClient.RequestHeadersSpec<?> withSecurityHeaders(WebClient.RequestHeadersSpec<?> req,
                                                              SecurityContext context) {
    return req
        .header("X-Auth-Token", context.authToken)
        .header("X-User-Id", context.userId);
  }

  private Mono<Login.Reply> login(Mono<Login> login) {
    return webClient.post()
        .uri("/api/v1/login")
        .contentType(MediaType.APPLICATION_JSON)
        .body(login, Login.class)
        .retrieve()
        .bodyToMono(Login.Reply.class);
  }

  private static class SecurityContext {
    private final String userId;
    private final String authToken;

    SecurityContext(String userId, String authToken) {
      this.userId = userId;
      this.authToken = authToken;
    }
  }

  private ExchangeFilterFunction logRequest() { //todo: switch to debug
    return (clientRequest, next) -> {
      log.info(">>> {} {}", clientRequest.method(), clientRequest.url());
      clientRequest.headers()
          .forEach((name, values) -> values.forEach(value -> log.info(">>> {}={}", name, value)));
      return next.exchange(clientRequest);
    };
  }

}
