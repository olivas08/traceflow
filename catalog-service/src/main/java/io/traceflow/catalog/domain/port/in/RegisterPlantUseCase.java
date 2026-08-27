package io.traceflow.catalog.domain.port.in;

import io.traceflow.catalog.domain.model.Plant;

public interface RegisterPlantUseCase {
    Plant execute(Command command);

    record Command(String code, String name, String country) {}
}
