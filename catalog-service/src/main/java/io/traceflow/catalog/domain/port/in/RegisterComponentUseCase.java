package io.traceflow.catalog.domain.port.in;

import io.traceflow.catalog.domain.model.Component;

public interface RegisterComponentUseCase {
    Component execute(Command command);

    record Command(
            String plantCode,
            String serialNumber,
            String partNumber,
            String parentSerialNumber,
            String correlationId) {}
}
