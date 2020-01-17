package rocketchat.spring.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoProcessor;
import reactor.netty.http.client.HttpClient;
import rocketchat.spring.ClientProperties;
import rocketchat.spring.model.IdOrName;
import rocketchat.spring.rest.messages.*;
import rocketchat.spring.rest.messages.reply.RoomInfoReply;

import javax.xml.bind.DatatypeConverter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
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

  public ReactiveRocketChatClientImpl(HttpClient httpClient, ClientProperties properties) {
    this.webClient = WebClient.builder()
        .baseUrl(properties.getBaseUrl())
        .filter(logRequest())
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .build();

    final Login login = new Login();
    login.setUser(properties.getUser());
    login.setPassword(properties.getPassword());
    this.login = login;
  }

  @Override
  public Mono<PostMessage.Reply> postMessage(Mono<PostMessage> message) {
    return doSecurePost("/api/v1/chat.postMessage", message, PostMessage.class, ReplyType.of(PostMessage.Reply.class));
  }

  @Override
  public Mono<ChannelReply> channelInfo(ChannelInfo info) {
    return doSecureGet(b -> b.path("/api/v1/channels.info")
        .queryParam(info.getFieldName(), info.getFieldValue()).build(), ReplyType.of(ChannelReply.class));
  }

  @Override
  public Mono<RoomInfoReply> roomInfo(IdOrName token) {
    return doSecureGet(b -> b.path("/api/v1/rooms.info")
            .queryParam(token.isId() ? "roomId" : "roomName", token.getValue()).build(),
        ReplyType.of(RoomInfoReply.class));
  }

  @Override
  public Mono<UserReply> userInfo(UserInfo info) {
    return doSecureGet(b -> b.path("/api/v1/users.info")
        .queryParam(info.getFieldName(), info.getFieldValue()).build(), ReplyType.of(UserReply.class));
  }

  @Override
  public Mono<ChannelReply> createChannel(Mono<CreateChannel> message) {
    return doSecurePost("/api/v1/channels.create", message, CreateChannel.class, ReplyType.of(ChannelReply.class));
  }

  @Override
  public Mono<ChannelReply> inviteUserToChannel(String roomId, String userId) {
    final Map<String, String> message = new HashMap<>();
    message.put("roomId", roomId);
    message.put("userId", userId);
    return doSecurePost("/api/v1/channels.invite", Mono.just(message), Map.class, ReplyType.of(ChannelReply.class));
  }

  @Override
  public Mono<ChannelReply> removeUserFromChannel(String roomId, String userId) {
    final Map<String, String> message = new HashMap<>(); //todo:
    message.put("roomId", roomId);
    message.put("userId", userId);
    return doSecurePost("/api/v1/channels.kick", Mono.just(message), Map.class, ReplyType.of(ChannelReply.class));
  }

  @Override
  public Mono<UserReply> createUser(Mono<CreateUser> message) {
    return doSecurePost("/api/v1/users.create", message, CreateUser.class, ReplyType.of(UserReply.class));
  }

  @Override
  public Mono<UserReply> updateOwnBasicInfo(Mono<UserBasicInfo> message) {
    final Mono<Map> data = message.map(info -> {
      final UserBasicInfo c = new UserBasicInfo(info);
      c.setCurrentPassword(sha256hex(this.login.getPassword()));
      return c;
    }).map(info -> Collections.singletonMap("data", info));
    return doSecurePost("/api/v1/users.updateOwnBasicInfo", data, Map.class, ReplyType.of(UserReply.class));
  }

  private static String sha256hex(String s) {
    try {
      final MessageDigest digest = MessageDigest.getInstance("SHA-256");
      final byte[] hash = digest.digest(s.getBytes(StandardCharsets.UTF_8));
      return DatatypeConverter.printHexBinary(hash).toLowerCase();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
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

  private <REQ, RES> Mono<RES> doSecurePost(String uri, Mono<REQ> body, Class<REQ> requestClass, ReplyType<RES> replyType) {
    final Supplier<WebClient.RequestHeadersSpec<?>> requestProvider = () -> post(uri, body, requestClass);
    return doExecute(replyType, requestProvider);
  }

  private <RES> Mono<RES> doSecureGet(Function<UriBuilder, URI> uriFunction, ReplyType<RES> replyType) {
    final Supplier<WebClient.RequestHeadersSpec<?>> requestProvider = () -> get(uriFunction);
    return doExecute(replyType, requestProvider);
  }

  private <RES> Mono<RES> doExecute(ReplyType<RES> replyType, Supplier<WebClient.RequestHeadersSpec<?>> requestProvider) {
    return
        securityContext()
            .flatMap(context -> withSecurityHeaders(requestProvider, context)
                .exchange()
                .flatMap(response -> {
                  if (response.statusCode() == HttpStatus.UNAUTHORIZED) {
                    return resetSecurityContext()
                        .flatMap(context2 -> {
                          final WebClient.ResponseSpec spec = withSecurityHeaders(requestProvider, context2).retrieve();
                          return replyType.isParametrized() ? spec.bodyToMono(replyType.getTypeReference())
                              : spec.bodyToMono(replyType.getType());
                        });
                  }
                  if (response.statusCode().is4xxClientError()) {
                    return Mono.error(new ClientHttpException(response));
                  }
                  if (response.statusCode().is5xxServerError()) {
                    return Mono.error(new ServerHttpException(response));
                  }
                  return replyType.isParametrized() ? response.bodyToMono(replyType.getTypeReference())
                      : response.bodyToMono(replyType.getType());
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

  private static class ReplyType<T> {
    private final Class<T> type;
    private final ParameterizedTypeReference<T> typeReference;

    private ReplyType(Class<T> type, ParameterizedTypeReference<T> typeReference) {
      this.type = type;
      this.typeReference = typeReference;
    }

    static <T> ReplyType<T> of(Class<T> type) {
      return new ReplyType<>(type, null);
    }

    static <T> ReplyType<T> of(ParameterizedTypeReference<T> type) {
      return new ReplyType<>(null, type);
    }

    private boolean isParametrized() {
      return this.typeReference != null;
    }

    private Class<T> getType() {
      return type;
    }

    private ParameterizedTypeReference<T> getTypeReference() {
      return typeReference;
    }
  }

}
