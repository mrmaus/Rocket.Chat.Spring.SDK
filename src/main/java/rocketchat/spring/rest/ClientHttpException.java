package rocketchat.spring.rest;

import org.springframework.web.reactive.function.client.ClientResponse;

public class ClientHttpException extends HttpException {

  ClientHttpException(ClientResponse response) {
    super(response);
  }
}
