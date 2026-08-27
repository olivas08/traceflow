package io.traceflow.catalog.application;

import io.traceflow.catalog.domain.exception.ComponentAlreadyExists;
import io.traceflow.catalog.domain.exception.ComponentNotFound;
import io.traceflow.catalog.domain.exception.PlantNotFound;
import io.traceflow.catalog.domain.model.Component;
import io.traceflow.catalog.domain.model.Plant;
import io.traceflow.catalog.domain.model.PlantCode;
import io.traceflow.catalog.domain.model.SerialNumber;
import io.traceflow.catalog.domain.model.TimelineEntry;
import io.traceflow.catalog.domain.port.in.RegisterComponentUseCase;
import io.traceflow.catalog.domain.port.out.ComponentRepository;
import io.traceflow.catalog.domain.port.out.DomainEventPublisher;
import io.traceflow.catalog.domain.port.out.PlantRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@ApplicationScoped
public class RegisterComponentService implements RegisterComponentUseCase {
    private final PlantRepository plants;
    private final ComponentRepository components;
    private final DomainEventPublisher events;

    public RegisterComponentService(
            PlantRepository plants, ComponentRepository components, DomainEventPublisher events) {
        this.plants = plants;
        this.components = components;
        this.events = events;
    }

    @Override
    @Transactional
    public Component execute(Command command) {
        Plant plant =
                plants.findByCode(new PlantCode(command.plantCode()))
                        .orElseThrow(() -> new PlantNotFound(command.plantCode()));
        SerialNumber serial = new SerialNumber(command.serialNumber());
        if (components.existsBySerial(serial)) {
            throw new ComponentAlreadyExists(serial.value());
        }
        SerialNumber parent = parentSerial(command.parentSerialNumber());
        Component component =
                Component.registered(serial, command.partNumber(), plant.id(), parent);
        components.save(component);
        components.appendTimeline(
                serial,
                new TimelineEntry(
                        UUID.randomUUID(),
                        Instant.now(),
                        "COMPONENT_REGISTERED",
                        "Component registered at plant " + plant.code().value(),
                        command.correlationId()));
        events.publishComponentRegistered(component, plant, command.correlationId());
        return component;
    }

    private SerialNumber parentSerial(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        SerialNumber parent = new SerialNumber(raw);
        if (!components.existsBySerial(parent)) {
            throw new ComponentNotFound(parent.value());
        }
        return parent;
    }
}
