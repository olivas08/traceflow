package io.traceflow.catalog.application;

import io.traceflow.catalog.domain.exception.ComponentNotFound;
import io.traceflow.catalog.domain.model.Component;
import io.traceflow.catalog.domain.model.InspectionOutcome;
import io.traceflow.catalog.domain.model.SerialNumber;
import io.traceflow.catalog.domain.model.TimelineEntry;
import io.traceflow.catalog.domain.port.in.ApplyInspectionResultUseCase;
import io.traceflow.catalog.domain.port.out.ComponentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@ApplicationScoped
public class ApplyInspectionResultService implements ApplyInspectionResultUseCase {
    private final ComponentRepository components;

    public ApplyInspectionResultService(ComponentRepository components) {
        this.components = components;
    }

    @Override
    @Transactional
    public void execute(Command command) {
        SerialNumber serial = new SerialNumber(command.serialNumber());
        Component component =
                components
                        .findBySerial(serial)
                        .orElseThrow(() -> new ComponentNotFound(serial.value()));
        InspectionOutcome outcome = InspectionOutcome.from(command.result());
        component.applyInspection(outcome);
        components.save(component);
        components.appendTimeline(
                serial,
                new TimelineEntry(
                        UUID.randomUUID(),
                        Instant.now(),
                        "INSPECTION_" + outcome.name(),
                        "Inspection " + outcome.name() + " by " + command.inspector(),
                        command.correlationId()));
    }
}
