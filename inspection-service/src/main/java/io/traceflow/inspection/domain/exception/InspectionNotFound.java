package io.traceflow.inspection.domain.exception;

public final class InspectionNotFound extends DomainException {
    public InspectionNotFound(String id) {
        super("Inspection not found: " + id);
    }
}
