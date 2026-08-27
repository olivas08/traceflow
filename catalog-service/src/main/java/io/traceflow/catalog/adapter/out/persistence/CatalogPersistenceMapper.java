package io.traceflow.catalog.adapter.out.persistence;

import io.traceflow.catalog.domain.model.Component;
import io.traceflow.catalog.domain.model.ComponentStatus;
import io.traceflow.catalog.domain.model.Plant;
import io.traceflow.catalog.domain.model.PlantCode;
import io.traceflow.catalog.domain.model.SerialNumber;
import io.traceflow.catalog.domain.model.TimelineEntry;

final class CatalogPersistenceMapper {
    private CatalogPersistenceMapper() {}

    static PlantEntity toEntity(Plant plant) {
        PlantEntity entity = new PlantEntity();
        entity.id = plant.id();
        entity.code = plant.code().value();
        entity.name = plant.name();
        entity.country = plant.country();
        return entity;
    }

    static Plant toDomain(PlantEntity entity) {
        return new Plant(entity.id, new PlantCode(entity.code), entity.name, entity.country);
    }

    static ComponentEntity toEntity(Component component) {
        ComponentEntity entity = new ComponentEntity();
        entity.id = component.id();
        entity.serialNumber = component.serialNumber().value();
        entity.partNumber = component.partNumber();
        entity.plantId = component.plantId();
        entity.parentSerial = component.parentSerial().map(SerialNumber::value).orElse(null);
        entity.status = component.status().name();
        return entity;
    }

    static Component toDomain(ComponentEntity entity) {
        SerialNumber parent =
                entity.parentSerial == null ? null : new SerialNumber(entity.parentSerial);
        return new Component(
                entity.id,
                new SerialNumber(entity.serialNumber),
                entity.partNumber,
                entity.plantId,
                parent,
                ComponentStatus.valueOf(entity.status));
    }

    static TimelineEntry toDomain(TimelineEntity entity) {
        return new TimelineEntry(
                entity.id, entity.occurredAt, entity.type, entity.summary, entity.correlationId);
    }
}
