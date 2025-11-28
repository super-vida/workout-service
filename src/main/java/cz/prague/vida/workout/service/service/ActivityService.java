package cz.prague.vida.workout.service.service;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import cz.prague.vida.workout.service.db.entity.ActivityEntity;
import cz.prague.vida.workout.service.db.repository.ActivityRepository;
import cz.prague.vida.workout.service.model.ApiActivityResponse;
import cz.prague.vida.workout.service.model.ApiActivityUpdate;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ActivityService {

  private final ActivityRepository activityRepository;

  public Flux<ApiActivityResponse> findAll() {
    return activityRepository.findAll().map(ActivityService::toResponse);
  }

  public Mono<ApiActivityResponse> findById(BigDecimal id) {
    return activityRepository.findById(id.longValue()).map(ActivityService::toResponse);
  }

  public Mono<ApiActivityResponse> save(Mono<ApiActivityUpdate> activity) {
    return activity
        .map(ActivityService::toEntity)
        .flatMap(activityRepository::save)
        .map(ActivityService::toResponse);
  }

  public Mono<ApiActivityResponse> update(BigDecimal id, Mono<ApiActivityUpdate> activity) {
    return activityRepository
        .findById(id.longValue())
        .switchIfEmpty(
            Mono.error(
                new ResponseStatusException(NOT_FOUND, "Activity with id " + id + " not found")))
        .zipWith(activity)
        .map(
            tuple ->
                ActivityEntity.builder()
                    .id(tuple.getT1().id())
                    .name(tuple.getT2().getName())
                    .build())
        .flatMap(activityRepository::save)
        .map(ActivityService::toResponse);
  }

  private static ApiActivityResponse toResponse(ActivityEntity entity) {
    return ApiActivityResponse.builder()
        .id(new BigDecimal(entity.id()))
        .name(entity.name())
        .build();
  }

  private static ActivityEntity toEntity(ApiActivityUpdate s) {
    return ActivityEntity.builder().name(s.getName()).build();
  }
}
