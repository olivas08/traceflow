package io.traceflow.inspection.domain.port.in;

import io.traceflow.inspection.domain.model.Inspection;

public interface RecordInspectionUseCase {
    Inspection execute(Command command);

    record Command(
            String serialNumber,
            String result,
            String inspector,
            String notes,
            String idempotencyKey,
            String correlationId) {}
}
