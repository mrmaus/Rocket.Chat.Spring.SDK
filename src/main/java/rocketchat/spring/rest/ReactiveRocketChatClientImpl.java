package rocketchat.spring.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoProcessor;
import rocketchat.spring.ClientProperties;
import rocketchat.spring.rest.messages.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class ReactiveRocketChatClientImpl implements ReactiveRocketChatClient {
  private static final Logger log = LoggerFactory.getLogger(ReactiveRocketChatClientImpl.class);

  //todo: default content type
  //todo: add response logger

  private final WebClient webClient;
  private final Login login;

  /**
   * Security context: the context contains authentication data required for certain REST calls
   */
  private volatile SecurityContext securityContext;

  public ReactiveRocketChatClientImpl(ClientProperties properties) {
    this.webClient = WebClient.builder()
        .baseUrl(properties.getBaseUrl())
        .filter(logRequest())
        .build();

    final Login login = new Login();
    login.setUser(properties.getUser());
    login.setPassword(properties.getPassword());
    this.login = login;
  }

  @Override
  public Mono<PostMessage.Reply> postMessage(Mono<PostMessage> message) {
    return doSecurePost("/api/v1/chat.postMessage", message, PostMessage.class, PostMessage.Reply.class);
  }

  @Override
  public Mono<ChannelReply> channelInfo(ChannelInfo info) {
    return doSecureGet(b -> b.path("/api/v1/channels.info")
        .queryParam(info.getFieldName(), info.getFieldValue()).build(), ChannelReply.class);
  }

  @Override
  public Mono<UserReply> userInfo(UserInfo info) {
    return doSecureGet(b -> b.path("/api/v1/users.info")
        .queryParam(info.getFieldName(), info.getFieldValue()).build(), UserReply.class);
  }

  @Override
  public Mono<ChannelReply> createChannel(Mono<CreateChannel> message) {
    return doSecurePost("/api/v1/channels.create", message, CreateChannel.class, ChannelReply.class);
  }

  @Override
  public Mono<ChannelReply> inviteUserToChannel(String roomId, String userId) {
    final Map<String, String> message = new HashMap<>();
    message.put("roomId", roomId);
    message.put("userId", userId);
    return doSecurePost("/api/v1/channels.invite", Mono.just(message), Map.class, ChannelReply.class);
  }

  @Override
  public Mono<ChannelReply> removeUserFromChannel(String roomId, String userId) {
    final Map<String, String> message = new HashMap<>(); //todo:
    message.put("roomId", roomId);
    message.put("userId", userId);
    return doSecurePost("/api/v1/channels.kick", Mono.just(message), Map.class, ChannelReply.class);
  }

  @Override
  public Mono<UserReply> createUser(Mono<CreateUser> message) {
    return doSecurePost("/api/v1/users.create", message, CreateUser.class, UserReply.class);
  }

  private Mono<SecurityContext> securityContext() {
    if (this.securityContext == null) {
      return resetSecurityContext();
    }
    return Mono.just(this.securityContext);
  }

  private Mono<SecurityContext> resetSecurityContext() {
    return MonoProcessor.fromCallable(() -> login)
        .flatMap(login -> login(Mono.just(login)))
        .map(reply -> new SecurityContext(
            reply.getData().getUserId(),
            reply.getData().getAuthToken())
        ).doOnNext(context -> this.securityContext = context);
  }

  private <REQ, RES> Mono<RES> doSecurePost(String uri, Mono<REQ> body, Class<REQ> requestClass, Class<RES> replyClass) {
    final Supplier<WebClient.RequestHeadersSpec<?>> requestProvider = () -> post(uri, body, requestClass);
    return doExecute(replyClass, requestProvider);
  }

  private <RES> Mono<RES> doSecureGet(Function<UriBuilder, URI> uriFunction, Class<RES> replyClass) {
    final Supplier<WebClient.RequestHeadersSpec<?>> requestProvider = () -> get(uriFunction);
    return doExecute(replyClass, requestProvider);
  }

  private <RES> Mono<RES> doExecute(Class<RES> replyClass, Supplier<WebClient.RequestHeadersSpec<?>> requestProvider) {
    return
        securityContext()
            .flatMap(context -> withSecurityHeaders(requestProvider, context)
                .exchange()
                .flatMap(response -> {
                  if (response.statusCode() == HttpStatus.UNAUTHORIZED) {
                    return resetSecurityContext()
                        .flatMap(context2 -> withSecurityHeaders(requestProvider, context2)
                            .retrieve().bodyToMono(replyClass));
                  }
                  //todo: handle other response codes
                  return response.bodyToMono(replyClass);
                }));
  }

  private <REQ> WebClient.RequestHeadersSpec<?> post(String uri, Mono<REQ> body, Class<REQ> requestClass) {
    return webClient.post()
        .uri(uri)
        .contentType(MediaType.APPLICATION_JSON)
        .body(body, requestClass);
  }

  private WebClient.RequestHeadersSpec<?> get(Function<UriBuilder, URI> uriFunction) {
    return webClient.get().uri(uriFunction).accept(MediaType.APPLICATION_JSON);
  }

  private WebClient.RequestHeadersSpec<?> withSecurityHeaders(Supplier<WebClient.RequestHeadersSpec<?>> req,
                                                              SecurityContext context) {
    return req.get()
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
