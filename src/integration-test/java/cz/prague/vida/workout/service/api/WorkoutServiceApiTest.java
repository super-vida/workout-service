package cz.prague.vida.workout.service.api;

import static org.assertj.core.api.Assertions.assertThat;

import cz.prague.vida.workout.service.test.AbstractIntegrationTest;
import cz.prague.vida.workout.service.test.Logging;
import io.netty.handler.logging.LogLevel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.test.web.reactive.server.WebTestClient;

class WorkoutServiceApiTest extends AbstractIntegrationTest {

  @Autowired WebTestClient webTestClient;

  @Test
  void activitiesApiTest(CapturedOutput output) {
    webTestClient
        .get()
        .uri("/activities")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .json(
            """
            [{
              id: "777888999",
              name: "Afternoon Ride"
            },
            {
              id: "444333555",
              name: "Afternoon Ride"
            }]
            """)
//        .jsonPath("timestamp")
//        .value(
//            OffsetDateTime.class,
//            t ->
//                assertThat(t)
//                    .isBetween(OffsetDateTime.now().minusMinutes(1), OffsetDateTime.now()))
                        ;

    assertThat(Logging.getLogs(output))
        .anySatisfy(
            log -> {
              assertThat(log.level()).isEqualTo(LogLevel.INFO.toString());
              assertThat(log.message()).contains("getActivities api delegate called");
            });
  }
}
