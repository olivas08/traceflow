package io.traceflow.inspection.application;

import io.traceflow.inspection.domain.exception.InspectionNotFound;
import io.traceflow.inspection.domain.model.Inspection;
import io.traceflow.inspection.domain.port.in.GetInspectionUseCase;
import io.traceflow.inspection.domain.port.out.InspectionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.UUID;

@ApplicationScoped
public class GetInspectionService implements GetInspectionUseCase {
    private final InspectionRepository inspections;

    public GetInspectionService(InspectionRepository inspections) {
        this.inspections = inspections;
    }

    @Override
    @Transactional
    public Inspection execute(UUID id) {
        return inspections.findById(id).orElseThrow(() -> new InspectionNotFound(id.toString()));
    }
}
