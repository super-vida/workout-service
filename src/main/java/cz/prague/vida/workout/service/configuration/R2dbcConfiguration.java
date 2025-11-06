package cz.prague.vida.workout.service.configuration;

import cz.prague.vida.workout.service.db.converter.InstantToLocalDateTimeConverter;
import cz.prague.vida.workout.service.db.converter.LocalDateTimeToInstantConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.DialectResolver;
import org.springframework.r2dbc.core.DatabaseClient;

@ConditionalOnResource(
    resources = "always_disabled") // TODO we need to have a test database first !!!
@Configuration(proxyBeanMethods = false)
@EnableR2dbcAuditing
public class R2dbcConfiguration {

  @Bean
  public R2dbcCustomConversions r2dbcCustomConversions(DatabaseClient client) {
    return R2dbcCustomConversions.of(
        DialectResolver.getDialect(client.getConnectionFactory()),
        new InstantToLocalDateTimeConverter(),
        new LocalDateTimeToInstantConverter());
  }
}
