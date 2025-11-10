package cz.prague.vida.workout.service.api.delegate;

import cz.prague.vida.workout.service.api.ActivitiesApiDelegate;
import cz.prague.vida.workout.service.model.ApiActivityResponse;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ActivityApiDelegateImpl implements ActivitiesApiDelegate {

  @Override
  public Mono<ResponseEntity<Flux<ApiActivityResponse>>> getActivities(ServerWebExchange exchange) {
    log.info("getActivities api delegate called");
    ApiActivityResponse activity1 =
        ApiActivityResponse.builder().id("777888999").name("Afternoon Ride").build();
    ApiActivityResponse activity2 =
        ApiActivityResponse.builder()
            .id("444333555")
            .name("Afternoon Ride")
            .timestamp(OffsetDateTime.now())
            .build();
    List<ApiActivityResponse> activities = List.of(activity1, activity2);
    Flux<ApiActivityResponse> hello = Flux.fromIterable(activities);

    return Mono.just(ResponseEntity.ok(hello));
  }
}
