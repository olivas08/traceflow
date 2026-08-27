package io.traceflow.inspection.domain.port.in;

import io.traceflow.inspection.domain.model.Inspection;
import io.traceflow.inspection.domain.model.PageResult;

public interface ListInspectionsUseCase {
    PageResult<Inspection> execute(int page, int size);
}
