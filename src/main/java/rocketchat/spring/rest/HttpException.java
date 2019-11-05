package rocketchat.spring.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

public class HttpException extends RuntimeException {

  private final HttpStatus status;
  private final Mono<String> body;

  HttpException(ClientResponse response) {
    super(response.statusCode().toString());
    status = response.statusCode();
    body = response.bodyToMono(String.class);
  }

  public HttpStatus getStatus() {
    return status;
  }

  public Mono<String> getBody() {
    return body;
  }
}
