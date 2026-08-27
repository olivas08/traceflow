package io.traceflow.catalog.application;

import io.traceflow.catalog.domain.exception.ComponentNotFound;
import io.traceflow.catalog.domain.model.Component;
import io.traceflow.catalog.domain.model.SerialNumber;
import io.traceflow.catalog.domain.port.in.GetComponentUseCase;
import io.traceflow.catalog.domain.port.out.ComponentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class GetComponentService implements GetComponentUseCase {
    private final ComponentRepository components;

    public GetComponentService(ComponentRepository components) {
        this.components = components;
    }

    @Override
    @Transactional
    public Component execute(String serialNumber) {
        SerialNumber serial = new SerialNumber(serialNumber);
        return components
                .findBySerial(serial)
                .orElseThrow(() -> new ComponentNotFound(serial.value()));
    }
}
