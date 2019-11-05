package rocketchat.spring.rest;

import org.springframework.web.reactive.function.client.ClientResponse;

public class ServerHttpException extends HttpException {
  ServerHttpException(ClientResponse response) {
    super(response);
  }
}
