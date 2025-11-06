package cz.prague.vida.workout.service.api.delegate;

import cz.prague.vida.workout.service.api.HelloApiDelegate;
import cz.prague.vida.workout.service.model.ApiHelloResponse;
import java.time.OffsetDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class HelloApiDelegateImpl implements HelloApiDelegate {

  @Override
  public Mono<ResponseEntity<ApiHelloResponse>> getHello(String name, ServerWebExchange exchange) {
    log.info("getHello api delegate called");
    var hello = ApiHelloResponse.builder().greeting("Hello").timestamp(OffsetDateTime.now());
    if (name != null) {
      hello.name(name);
    }
    return Mono.just(ResponseEntity.ok(hello.build()));
  }
}
