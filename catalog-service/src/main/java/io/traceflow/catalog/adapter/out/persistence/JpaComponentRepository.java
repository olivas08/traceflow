package io.traceflow.catalog.adapter.out.persistence;

import io.traceflow.catalog.domain.model.Component;
import io.traceflow.catalog.domain.model.PageResult;
import io.traceflow.catalog.domain.model.SerialNumber;
import io.traceflow.catalog.domain.model.TimelineEntry;
import io.traceflow.catalog.domain.port.out.ComponentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JpaComponentRepository implements ComponentRepository {
    @Override
    public void save(Component component) {
        ComponentEntity entity = CatalogPersistenceMapper.toEntity(component);
        ComponentEntity.getEntityManager().merge(entity);
    }

    @Override
    public boolean existsBySerial(SerialNumber serialNumber) {
        return ComponentEntity.count("serialNumber", serialNumber.value()) > 0;
    }

    @Override
    public Optional<Component> findBySerial(SerialNumber serialNumber) {
        return ComponentEntity.find("serialNumber", serialNumber.value())
                .firstResultOptional()
                .map(entity -> CatalogPersistenceMapper.toDomain((ComponentEntity) entity));
    }

    @Override
    public PageResult<Component> findAll(int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = size < 1 ? 20 : Math.min(size, 100);
        List<Component> items =
                ComponentEntity.findAll().page(safePage, safeSize).list().stream()
                        .map(entity -> CatalogPersistenceMapper.toDomain((ComponentEntity) entity))
                        .toList();
        return new PageResult<>(items, ComponentEntity.count(), safePage, safeSize);
    }

    @Override
    public void appendTimeline(SerialNumber serialNumber, TimelineEntry entry) {
        TimelineEntity entity = new TimelineEntity();
        entity.id = entry.id();
        entity.serialNumber = serialNumber.value();
        entity.occurredAt = entry.occurredAt();
        entity.type = entry.type();
        entity.summary = entry.summary();
        entity.correlationId = entry.correlationId();
        entity.persist();
    }

    @Override
    public List<TimelineEntry> findTimeline(SerialNumber serialNumber) {
        return TimelineEntity.find(
                        "serialNumber = ?1 order by occurredAt asc", serialNumber.value())
                .list()
                .stream()
                .map(entity -> CatalogPersistenceMapper.toDomain((TimelineEntity) entity))
                .toList();
    }
}
