package io.traceflow.inspection.domain.port.in;

import io.traceflow.inspection.domain.model.Inspection;
import java.util.UUID;

public interface GetInspectionUseCase {
    Inspection execute(UUID id);
}
