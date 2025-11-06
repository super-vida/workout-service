package cz.prague.vida.workout.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Hooks;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class ReactiveAppSetup {

  public static void setup() {
    Hooks.onErrorDropped(throwable -> log.warn("Operator called onErrorDropped", throwable));
  }
}
