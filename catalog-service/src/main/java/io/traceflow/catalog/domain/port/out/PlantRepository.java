package io.traceflow.catalog.domain.port.out;

import io.traceflow.catalog.domain.model.PageResult;
import io.traceflow.catalog.domain.model.Plant;
import io.traceflow.catalog.domain.model.PlantCode;
import java.util.Optional;
import java.util.UUID;

public interface PlantRepository {
    void save(Plant plant);

    boolean existsByCode(PlantCode code);

    Optional<Plant> findByCode(PlantCode code);

    Optional<Plant> findById(UUID id);

    PageResult<Plant> findAll(int page, int size);
}
