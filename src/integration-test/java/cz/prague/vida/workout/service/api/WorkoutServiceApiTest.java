package cz.prague.vida.workout.service.api;

import static org.assertj.core.api.Assertions.assertThat;

import cz.prague.vida.workout.service.db.entity.ActivityEntity;
import cz.prague.vida.workout.service.db.repository.ActivityRepository;
import cz.prague.vida.workout.service.test.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

class WorkoutServiceApiTest extends AbstractIntegrationTest {

  @Autowired WebTestClient webTestClient;

  @Autowired ActivityRepository activityRepository;

  @BeforeEach
  void setUp() {
    // Clean up database before each test
    activityRepository.deleteAll().block();
  }

  @Test
  void shouldGetAllExamples() {
    // Given: Create test data
    ActivityEntity workout1 =
        activityRepository
            .save(ActivityEntity.builder().name("Workout 1").build())
            .block();
    ActivityEntity workout2 =
        activityRepository
            .save(ActivityEntity.builder().name("Workout 2").build())
            .block();

    assertThat(workout1).isNotNull();
    assertThat(workout2).isNotNull();

    // When & Then
    webTestClient
        .get()
        .uri("/activity")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$[0].id")
        .isEqualTo(workout1.id())
        .jsonPath("$[0].name")
        .isEqualTo("Workout 1")
        .jsonPath("$[1].id")
        .isEqualTo(workout2.id())
        .jsonPath("$[1].name")
        .isEqualTo("Workout 2");
  }

  @Test
  void shouldGetEmptyListWhenNoExamples() {
    // When & Then
    webTestClient.get().uri("/activity").exchange().expectStatus().isOk().expectBody().json("[]");
  }
}
