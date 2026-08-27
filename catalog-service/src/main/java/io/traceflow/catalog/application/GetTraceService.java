package io.traceflow.catalog.application;

import io.traceflow.catalog.domain.exception.ComponentNotFound;
import io.traceflow.catalog.domain.model.Component;
import io.traceflow.catalog.domain.model.ComponentTrace;
import io.traceflow.catalog.domain.model.SerialNumber;
import io.traceflow.catalog.domain.port.in.GetTraceUseCase;
import io.traceflow.catalog.domain.port.out.ComponentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class GetTraceService implements GetTraceUseCase {
    private final ComponentRepository components;

    public GetTraceService(ComponentRepository components) {
        this.components = components;
    }

    @Override
    @Transactional
    public ComponentTrace execute(String serialNumber) {
        SerialNumber serial = new SerialNumber(serialNumber);
        Component component =
                components
                        .findBySerial(serial)
                        .orElseThrow(() -> new ComponentNotFound(serial.value()));
        return new ComponentTrace(component, components.findTimeline(serial));
    }
}
