package io.traceflow.catalog.domain.port.in;

public interface ApplyInspectionResultUseCase {
    void execute(Command command);

    record Command(
            String serialNumber,
            String result,
            String inspector,
            String correlationId,
            String eventId) {}
}
