package cz.prague.vida.workout.service.db.converter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.NonNull;

@WritingConverter
public class InstantToLocalDateTimeConverter implements Converter<Instant, LocalDateTime> {

  @Override
  public LocalDateTime convert(@NonNull Instant instant) {
    return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
  }
}
