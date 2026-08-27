package io.traceflow.catalog.adapter.in.rest;

import io.traceflow.catalog.domain.model.Component;
import io.traceflow.catalog.domain.model.ComponentTrace;
import io.traceflow.catalog.domain.model.PageResult;
import io.traceflow.catalog.domain.model.Plant;
import io.traceflow.catalog.domain.model.SerialNumber;
import io.traceflow.catalog.domain.model.TimelineEntry;
import java.util.List;

final class CatalogRestMapper {
    private CatalogRestMapper() {}

    static PlantResponse toResponse(Plant plant) {
        return new PlantResponse(plant.id(), plant.code().value(), plant.name(), plant.country());
    }

    static ComponentResponse toResponse(Component component) {
        return new ComponentResponse(
                component.id(),
                component.serialNumber().value(),
                component.partNumber(),
                component.plantId(),
                component.parentSerial().map(SerialNumber::value).orElse(null),
                component.status().name());
    }

    static TimelineResponse toResponse(TimelineEntry entry) {
        return new TimelineResponse(
                entry.id(),
                entry.occurredAt(),
                entry.type(),
                entry.summary(),
                entry.correlationId());
    }

    static TraceResponse toResponse(ComponentTrace trace) {
        List<TimelineResponse> timeline =
                trace.timeline().stream().map(CatalogRestMapper::toResponse).toList();
        return new TraceResponse(toResponse(trace.component()), timeline);
    }

    static <T, R> PageResponse<R> toPage(
            PageResult<T> page, java.util.function.Function<T, R> mapper) {
        return new PageResponse<>(
                page.items().stream().map(mapper).toList(), page.total(), page.page(), page.size());
    }
}
