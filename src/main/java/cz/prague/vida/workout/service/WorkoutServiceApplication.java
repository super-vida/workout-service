package cz.prague.vida.workout.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WorkoutServiceApplication {

  public static void main(String[] args) {
    ReactiveAppSetup.setup();
    SpringApplication.run(WorkoutServiceApplication.class, args);
  }
}
