package io.traceflow.inspection.application;

import io.traceflow.inspection.domain.model.Inspection;
import io.traceflow.inspection.domain.model.PageResult;
import io.traceflow.inspection.domain.port.in.ListInspectionsUseCase;
import io.traceflow.inspection.domain.port.out.InspectionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ListInspectionsService implements ListInspectionsUseCase {
    private final InspectionRepository inspections;

    public ListInspectionsService(InspectionRepository inspections) {
        this.inspections = inspections;
    }

    @Override
    @Transactional
    public PageResult<Inspection> execute(int page, int size) {
        return inspections.findAll(page, size);
    }
}
