package cz.prague.vida.workout.service;

import static org.assertj.core.api.Assertions.assertThat;

import cz.prague.vida.workout.service.test.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WorkoutServiceApplicationTests extends AbstractIntegrationTest {

  @Autowired ApplicationContext applicationContext;

  @Test
  void contextLoads() {
    assertThat(applicationContext.getId()).isNotNull();
  }
}
