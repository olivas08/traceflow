package io.traceflow.inspection.application;

import io.traceflow.inspection.domain.model.Inspection;
import io.traceflow.inspection.domain.port.out.DomainEventPublisher;
import java.util.ArrayList;
import java.util.List;

final class RecordingEventPublisher implements DomainEventPublisher {
    final List<String> completed = new ArrayList<>();
    final List<String> failed = new ArrayList<>();

    @Override
    public void publishCompleted(Inspection inspection, String correlationId) {
        completed.add(inspection.serialNumber().value());
    }

    @Override
    public void publishFailed(Inspection inspection, String correlationId) {
        failed.add(inspection.serialNumber().value());
    }
}
