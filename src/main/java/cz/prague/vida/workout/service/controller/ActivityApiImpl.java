package cz.prague.vida.workout.service.controller;

import cz.prague.vida.workout.service.api.ActivityApi;
import cz.prague.vida.workout.service.model.ApiActivityResponse;
import cz.prague.vida.workout.service.model.ApiActivityUpdate;
import cz.prague.vida.workout.service.service.ActivityService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ActivityApiImpl implements ActivityApi {

  private final ActivityService activityService;

  @Override
  public Mono<ResponseEntity<Flux<ApiActivityResponse>>> getActivities(ServerWebExchange exchange) {
    log.info("getActivities called");
    return Mono.just(ResponseEntity.ok(activityService.findAll()));
  }

  @Override
  public Mono<ResponseEntity<ApiActivityResponse>> getActivity(
      BigDecimal id, ServerWebExchange exchange) {
    return activityService.findById(id).map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<ApiActivityResponse>> createActivity(
      Mono<ApiActivityUpdate> activity, ServerWebExchange exchange) {
    log.info("createActivity called");
    return activityService.save(activity).map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<ApiActivityResponse>> updateActivity(
      BigDecimal id, Mono<ApiActivityUpdate> activity, ServerWebExchange exchange) {
    return activityService.update(id, activity).map(ResponseEntity::ok);
  }
}
