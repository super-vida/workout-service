package cz.prague.vida.workout.service.configuration;

import static cz.prague.vida.workout.service.utils.CommonStructuredArguments.*;

import cz.prague.vida.workout.service.utils.LogContext;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({ServerProperties.class, WebProperties.class})
public class ErrorHandlingConfiguration {

  @Bean
  @Order(-1)
  public ErrorWebExceptionHandler errorWebExceptionHandler(
      ErrorAttributes errorAttributes,
      ServerProperties serverProperties,
      WebProperties webProperties,
      ObjectProvider<ViewResolver> viewResolvers,
      ServerCodecConfigurer serverCodecConfigurer,
      ApplicationContext applicationContext) {
    var exceptionHandler =
        new DefaultLoggingErrorWebExceptionHandler(
            errorAttributes,
            webProperties.getResources(),
            serverProperties.getError(),
            applicationContext);
    exceptionHandler.setViewResolvers(viewResolvers.orderedStream().toList());
    exceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
    exceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
    return exceptionHandler;
  }

  @Slf4j
  public static class DefaultLoggingErrorWebExceptionHandler
      extends DefaultErrorWebExceptionHandler {

    public DefaultLoggingErrorWebExceptionHandler(
        ErrorAttributes errorAttributes,
        WebProperties.Resources resources,
        ErrorProperties errorProperties,
        ApplicationContext applicationContext) {
      super(errorAttributes, resources, errorProperties, applicationContext);
    }

    @Override
    protected void logError(ServerRequest request, ServerResponse response, Throwable throwable) {
      if (response.statusCode().is4xxClientError()) {
        logClientError(log, throwable, response.statusCode(), request.exchange());
      } else if (response.statusCode().is5xxServerError()) {
        logServerError(log, throwable, response.statusCode(), request.exchange());
      }
    }
  }

  @ControllerAdvice
  @Slf4j
  @RequiredArgsConstructor
  public static class ProblemDetailsLoggingExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public final Mono<ResponseEntity<Object>> handleConstraintViolationException(
        ConstraintViolationException ex, ServerWebExchange exchange) {
      return handleExceptionInternal(
          ex,
          ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid request params."),
          null,
          HttpStatus.BAD_REQUEST,
          exchange);
    }

    @NonNull
    @Override
    protected Mono<ResponseEntity<Object>> handleExceptionInternal(
        @NonNull Exception ex,
        @Nullable Object body,
        @Nullable HttpHeaders headers,
        HttpStatusCode status,
        @NonNull ServerWebExchange exchange) {
      if (status.is4xxClientError()) {
        logClientError(log, ex, status, exchange);
      } else if (status.is5xxServerError()) {
        logServerError(log, ex, status, exchange);
      }
      return super.handleExceptionInternal(ex, body, headers, status, exchange);
    }
  }

  private static void logClientError(
      Logger logger, Throwable ex, HttpStatusCode status, ServerWebExchange exchange) {
    logger.warn(
        "{} {} Client error for {}",
        kvResponseStatusCode(status),
        kvLogContext(LogContext.CLIENT_ERROR_4XX),
        kvRequestDetailsRedacted(exchange),
        ex);
  }

  private static void logServerError(
      Logger logger, Throwable ex, HttpStatusCode status, ServerWebExchange exchange) {
    logger.error(
        "{} {} Server error for {}",
        kvResponseStatusCode(status),
        kvLogContext(LogContext.SERVER_ERROR_5XX),
        kvRequestDetailsRedacted(exchange),
        ex);
  }
}
