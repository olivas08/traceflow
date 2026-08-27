package io.traceflow.catalog.domain.port.in;

import io.traceflow.catalog.domain.model.Component;
import io.traceflow.catalog.domain.model.PageResult;

public interface ListComponentsUseCase {
    PageResult<Component> execute(int page, int size);
}
