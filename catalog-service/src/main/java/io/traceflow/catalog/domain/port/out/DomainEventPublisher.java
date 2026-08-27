package io.traceflow.catalog.domain.port.out;

import io.traceflow.catalog.domain.model.Component;
import io.traceflow.catalog.domain.model.Plant;

public interface DomainEventPublisher {
    void publishComponentRegistered(Component component, Plant plant, String correlationId);
}
