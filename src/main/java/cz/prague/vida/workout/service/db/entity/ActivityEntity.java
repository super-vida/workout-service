package cz.prague.vida.workout.service.db.entity;

import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("ACTIVITY")
@Builder
public record ActivityEntity(@Id @Column("ID") Long id, @NotNull @Column("NAME") String name) {

  @Override
  public boolean equals(Object o) {
    return id != null && o instanceof ActivityEntity that && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
