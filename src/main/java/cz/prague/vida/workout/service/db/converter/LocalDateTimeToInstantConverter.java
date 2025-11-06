package cz.prague.vida.workout.service.db.converter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class LocalDateTimeToInstantConverter implements Converter<LocalDateTime, Instant> {

  @Override
  public Instant convert(LocalDateTime localDateTime) {
    return localDateTime.toInstant(ZoneOffset.UTC);
  }
}
