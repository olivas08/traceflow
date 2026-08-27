package io.traceflow.inspection.application;

import io.traceflow.inspection.domain.model.Inspection;
import io.traceflow.inspection.domain.model.InspectionResult;
import io.traceflow.inspection.domain.model.SerialNumber;
import io.traceflow.inspection.domain.port.in.RecordInspectionUseCase;
import io.traceflow.inspection.domain.port.out.DomainEventPublisher;
import io.traceflow.inspection.domain.port.out.InspectionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class RecordInspectionService implements RecordInspectionUseCase {
    private final InspectionRepository inspections;
    private final DomainEventPublisher events;

    public RecordInspectionService(InspectionRepository inspections, DomainEventPublisher events) {
        this.inspections = inspections;
        this.events = events;
    }

    @Override
    @Transactional
    public Inspection execute(Command command) {
        if (command.idempotencyKey() != null && !command.idempotencyKey().isBlank()) {
            var existing = inspections.findByIdempotencyKey(command.idempotencyKey());
            if (existing.isPresent()) {
                return existing.get();
            }
        }
        Inspection inspection =
                Inspection.record(
                        new SerialNumber(command.serialNumber()),
                        InspectionResult.from(command.result()),
                        command.inspector(),
                        command.notes(),
                        blankToNull(command.idempotencyKey()));
        inspections.save(inspection);
        events.publishCompleted(inspection, command.correlationId());
        if (inspection.result().failed()) {
            events.publishFailed(inspection, command.correlationId());
        }
        return inspection;
    }

    private static String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value;
    }
}
