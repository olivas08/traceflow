package io.traceflow.inspection.domain.port.out;

import io.traceflow.inspection.domain.model.Inspection;
import io.traceflow.inspection.domain.model.PageResult;
import java.util.Optional;
import java.util.UUID;

public interface InspectionRepository {
    void save(Inspection inspection);

    Optional<Inspection> findById(UUID id);

    Optional<Inspection> findByIdempotencyKey(String key);

    PageResult<Inspection> findAll(int page, int size);
}
