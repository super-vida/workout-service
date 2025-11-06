package cz.prague.vida.workout.service.configuration.properties;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("gp-webpay-surcharge")
@Validated
public record SurchargeConfigurationProperties(@NotNull String exampleProperty) {}
