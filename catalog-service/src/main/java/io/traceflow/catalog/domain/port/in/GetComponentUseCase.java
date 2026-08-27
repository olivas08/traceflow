package io.traceflow.catalog.domain.port.in;

import io.traceflow.catalog.domain.model.Component;

public interface GetComponentUseCase {
    Component execute(String serialNumber);
}
