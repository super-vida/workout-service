package cz.prague.vida.workout.service.db.repository;

import cz.prague.vida.workout.service.db.entity.ActivityEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface ActivityRepository extends R2dbcRepository<ActivityEntity, Long> {}
