package io.traceflow.inspection.adapter.out.persistence;

import io.traceflow.inspection.domain.model.Inspection;
import io.traceflow.inspection.domain.model.InspectionResult;
import io.traceflow.inspection.domain.model.PageResult;
import io.traceflow.inspection.domain.model.SerialNumber;
import io.traceflow.inspection.domain.port.out.InspectionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class JpaInspectionRepository implements InspectionRepository {
    @Override
    public void save(Inspection inspection) {
        InspectionEntity entity = new InspectionEntity();
        entity.id = inspection.id();
        entity.serialNumber = inspection.serialNumber().value();
        entity.result = inspection.result().name();
        entity.inspector = inspection.inspector();
        entity.notes = inspection.notes();
        entity.inspectedAt = inspection.inspectedAt();
        entity.idempotencyKey = inspection.idempotencyKey();
        InspectionEntity.getEntityManager().merge(entity);
    }

    @Override
    public Optional<Inspection> findById(UUID id) {
        return InspectionEntity.findByIdOptional(id)
                .map(entity -> toDomain((InspectionEntity) entity));
    }

    @Override
    public Optional<Inspection> findByIdempotencyKey(String key) {
        return InspectionEntity.find("idempotencyKey", key)
                .firstResultOptional()
                .map(entity -> toDomain((InspectionEntity) entity));
    }

    @Override
    public PageResult<Inspection> findAll(int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = size < 1 ? 20 : Math.min(size, 100);
        List<Inspection> items =
                InspectionEntity.findAll().page(safePage, safeSize).list().stream()
                        .map(entity -> toDomain((InspectionEntity) entity))
                        .toList();
        return new PageResult<>(items, InspectionEntity.count(), safePage, safeSize);
    }

    private Inspection toDomain(InspectionEntity entity) {
        return new Inspection(
                entity.id,
                new SerialNumber(entity.serialNumber),
                InspectionResult.from(entity.result),
                entity.inspector,
                entity.notes,
                entity.inspectedAt,
                entity.idempotencyKey);
    }
}
