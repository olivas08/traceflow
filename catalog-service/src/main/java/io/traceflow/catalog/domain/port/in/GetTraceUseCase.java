package io.traceflow.catalog.domain.port.in;

import io.traceflow.catalog.domain.model.ComponentTrace;

public interface GetTraceUseCase {
    ComponentTrace execute(String serialNumber);
}
