package cz.prague.vida.workout.service.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Headers {

  private static final Set<String> HEADER_NAMES_TO_REDACT =
      Set.of(HttpHeaders.AUTHORIZATION.toLowerCase());
  private static final String REDACTED_VALUE = "redacted";

  public static HttpHeaders redact(HttpHeaders headers) {
    Assert.notNull(headers, "headers must not be null");
    return headers.entrySet().stream()
        .map(entry -> shouldRedact(entry) ? getRedacted(entry) : entry)
        .collect(
            Collectors.toMap(
                Map.Entry::getKey, Map.Entry::getValue, Headers::merge, HttpHeaders::new));
  }

  private static boolean shouldRedact(Map.Entry<String, List<String>> entry) {
    return HEADER_NAMES_TO_REDACT.contains(entry.getKey().toLowerCase());
  }

  private static Map.Entry<String, List<String>> getRedacted(
      Map.Entry<String, List<String>> entry) {
    return Map.entry(
        entry.getKey(), entry.getValue().stream().map(original -> REDACTED_VALUE).toList());
  }

  private static List<String> merge(List<String> a, List<String> b) {
    a.addAll(b);
    return a;
  }
}
