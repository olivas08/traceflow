package io.traceflow.catalog.domain.port.in;

import io.traceflow.catalog.domain.model.PageResult;
import io.traceflow.catalog.domain.model.Plant;

public interface ListPlantsUseCase {
    PageResult<Plant> execute(int page, int size);
}
