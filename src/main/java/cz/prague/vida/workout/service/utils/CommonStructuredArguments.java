package cz.prague.vida.workout.service.utils;

import static net.logstash.logback.argument.StructuredArguments.kv;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.logstash.logback.argument.StructuredArgument;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ServerWebExchange;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonStructuredArguments {

  public static StructuredArgument kvLogContext(LogContext logContext) {
    return kv("logContext", logContext);
  }

  public static StructuredArgument kvResponseStatusCode(HttpStatusCode httpStatusCode) {
    return kv("httpResponseStatusCode", httpStatusCode.value());
  }

  public static StructuredArgument kvRequestDetailsRedacted(ServerWebExchange serverWebExchange) {
    return kv("httpRequest", requestDetailsOf(serverWebExchange));
  }

  private static HttpRequestDetails requestDetailsOf(ServerWebExchange serverWebExchange) {
    return HttpRequestDetails.builder()
        .httpMethod(serverWebExchange.getRequest().getMethod().toString())
        .uri(serverWebExchange.getRequest().getURI().toString())
        .path(serverWebExchange.getRequest().getPath().pathWithinApplication().value())
        .headers(
            cz.prague.vida.workout.service.utils.Headers.redact(
                    serverWebExchange.getRequest().getHeaders())
                .entrySet()
                .toString())
        .build();
  }

  @Builder
  public record HttpRequestDetails(String httpMethod, String uri, String path, String headers) {}
}
