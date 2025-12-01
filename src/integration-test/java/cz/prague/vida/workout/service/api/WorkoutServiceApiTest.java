package cz.prague.vida.workout.service.api;

import cz.prague.vida.workout.service.db.entity.ActivityEntity;
import cz.prague.vida.workout.service.db.repository.ActivityRepository;
import cz.prague.vida.workout.service.test.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

class WorkoutServiceApiTest extends AbstractIntegrationTest {

  @Autowired
  WebTestClient webTestClient;

  @Autowired
  ActivityRepository activityRepository;

  @BeforeEach
  void setUp() {
    // Clean up database before each test
    activityRepository.deleteAll().block();
  }

  @Test
  void shouldGetAllActivities() {
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
  void shouldGetEmptyListWhenNoActivities() {
    // When & Then
    webTestClient.get().uri("/activity").exchange().expectStatus().isOk().expectBody().json("[]");
  }

  @Test
  void shouldGetActivityById() {
    // Given: Create test data
    ActivityEntity activity =
      activityRepository.save(ActivityEntity.builder().name("Test Activity").build()).block();

    assertThat(activity).isNotNull();
    Long activityId = activity.id();

    // When & Then
    webTestClient
      .get()
      .uri("/activity/{id}", activityId)
      .exchange()
      .expectStatus()
      .isOk()
      .expectBody()
      .jsonPath("$.id")
      .isEqualTo(activityId)
      .jsonPath("$.name")
      .isEqualTo("Test Activity");
  }

  @Test
  void shouldCreateActivity() {
    // Given
    String requestBody =
      """
        {
          "name": "New Workout"
        }
        """;

    // When & Then
    webTestClient
      .post()
      .uri("/activity")
      .header("Content-Type", "application/json; charset=utf-8")
      .bodyValue(requestBody)
      .exchange()
      .expectStatus()
      .isOk()
      .expectBody()
      .jsonPath("$.id")
      .exists()
      .jsonPath("$.name")
      .isEqualTo("New Workout");

    // Verify in database
    StepVerifier.create(activityRepository.findAll())
      .assertNext(
        entity -> {
          assertThat(entity.name()).isEqualTo("New Workout");
          assertThat(entity.id()).isNotNull();
        })
      .verifyComplete();
  }

  @Test
  void shouldUpdateExistingActivity() {
    // Given: Create initial activity
    ActivityEntity activity =
      activityRepository.save(ActivityEntity.builder().name("Original Name").build()).block();

    assertThat(activity).isNotNull();
    Long activityId = activity.id();

    String updateBody =
      """
        {
          "name": "Updated Name"
        }
        """;

    // When & Then
    webTestClient
      .put()
      .uri("/activity/" + activityId)
      .header("Content-Type", "application/json; charset=utf-8")
      .bodyValue(updateBody)
      .exchange()
      .expectStatus()
      .isOk()
      .expectBody()
      .jsonPath("$.id")
      .isEqualTo(activityId)
      .jsonPath("$.name")
      .isEqualTo("Updated Name");

    // Verify in database
    StepVerifier.create(activityRepository.findById(activityId))
      .assertNext(
        entity -> {
          assertThat(entity.name()).isEqualTo("Updated Name");
          assertThat(entity.id()).isEqualTo(activityId);
        })
      .verifyComplete();
  }

  @Test
  void shouldReturnNotFoundWhenUpdatingNonExistentActivity() {
    // Given
    long nonExistentId = 999999L;
    String updateBody =
      """
        {
          "name": "Updated Name"
        }
        """;

    // When & Then
    webTestClient
      .put()
      .uri("/activity/{id}", nonExistentId)
      .header("Content-Type", "application/json; charset=utf-8")
      .bodyValue(updateBody)
      .exchange()
      .expectStatus()
      .isNotFound();
  }

  @Test
  void shouldHandleMultipleActivitiesInGetAll() {
    // Given: Create multiple activities
    for (int i = 1; i <= 5; i++) {
      activityRepository.save(ActivityEntity.builder().name("Workout " + i).build()).block();
    }

    // When & Then
    webTestClient
      .get()
      .uri("/activity")
      .exchange()
      .expectStatus()
      .isOk()
      .expectBody()
      .jsonPath("$.length()")
      .isEqualTo(5)
      .jsonPath("$[0].name")
      .exists()
      .jsonPath("$[4].name")
      .exists();

    // Verify all activities are returned
    StepVerifier.create(activityRepository.count()).expectNext(5L).verifyComplete();
  }

}
