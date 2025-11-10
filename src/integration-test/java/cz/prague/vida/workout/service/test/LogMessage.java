package cz.prague.vida.workout.service.test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LogMessage(
    String traceId,
    String message,
    String level,
    @JsonProperty("stack_trace") String stackTrace,
    @JsonProperty("logger_name") String loggerName,
    Integer httpResponseStatusCode,
    String logContext) {}
