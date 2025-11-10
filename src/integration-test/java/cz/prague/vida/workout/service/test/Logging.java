package cz.prague.vida.workout.service.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.boot.test.system.CapturedOutput;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Logging {

  public static List<LogMessage> getLogs(CapturedOutput capturedOutput) {
    var objectMapper = new ObjectMapper();
    return Arrays.stream(capturedOutput.getAll().split(System.lineSeparator()))
        .map(
            log -> {
              try {
                return Optional.of(objectMapper.readValue(log, LogMessage.class));
              } catch (JsonProcessingException e) {
                return Optional.<LogMessage>empty();
              }
            })
        .flatMap(Optional::stream)
        .toList();
  }
}
