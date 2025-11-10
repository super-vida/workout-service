package cz.gpe.prague.vida.workout.service;

import cz.prague.vida.workout.service.api.delegate.ActivityApiDelegateImpl;
import cz.prague.vida.workout.service.model.ApiActivityResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.test.StepVerifier;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class WorkoutServiceApiDelegateTest {

  @Mock
  private ServerWebExchange serverWebExchange;

  @InjectMocks
  private ActivityApiDelegateImpl helloApiDelegate;

  @Test
  void testActivitiesApiDelegate() {
    var result = helloApiDelegate.getActivities(serverWebExchange);

    StepVerifier.create(result)
      .assertNext(entity -> {
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<ApiActivityResponse> activities = entity.getBody().collectList().block();
        assertThat(activities).hasSize(2);
        assertThat(activities)
          .extracting(ApiActivityResponse::getId)
          .containsExactly("777888999", "444333555");
        assertThat(activities)
          .extracting(ApiActivityResponse::getName)
          .containsOnly("Afternoon Ride");
      })
      .verifyComplete();
  }
}
