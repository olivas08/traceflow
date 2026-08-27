package io.traceflow.catalog.application;

import io.traceflow.catalog.domain.model.Component;
import io.traceflow.catalog.domain.model.Plant;
import io.traceflow.catalog.domain.port.out.DomainEventPublisher;
import java.util.ArrayList;
import java.util.List;

final class RecordingEventPublisher implements DomainEventPublisher {
    final List<String> serials = new ArrayList<>();

    @Override
    public void publishComponentRegistered(Component component, Plant plant, String correlationId) {
        serials.add(component.serialNumber().value());
    }
}
