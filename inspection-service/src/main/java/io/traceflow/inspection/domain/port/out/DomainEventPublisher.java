package io.traceflow.inspection.domain.port.out;

import io.traceflow.inspection.domain.model.Inspection;

public interface DomainEventPublisher {
    void publishCompleted(Inspection inspection, String correlationId);

    void publishFailed(Inspection inspection, String correlationId);
}
