package io.traceflow.catalog.adapter.out.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.traceflow.catalog.adapter.out.persistence.OutboxEntity;
import io.traceflow.catalog.domain.model.Component;
import io.traceflow.catalog.domain.model.Plant;
import io.traceflow.catalog.domain.port.out.DomainEventPublisher;
import io.traceflow.common.kafka.ComponentRegisteredEvent;
import io.traceflow.common.kafka.Topics;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.Instant;
import java.util.UUID;

@ApplicationScoped
public class OutboxEventPublisher implements DomainEventPublisher {
    private final ObjectMapper objectMapper;

    public OutboxEventPublisher(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void publishComponentRegistered(Component component, Plant plant, String correlationId) {
        UUID eventId = UUID.randomUUID();
        ComponentRegisteredEvent payload =
                new ComponentRegisteredEvent(
                        eventId,
                        Instant.now(),
                        correlationId,
                        component.serialNumber().value(),
                        component.partNumber(),
                        plant.code().value(),
                        component.parentSerial().map(serial -> serial.value()).orElse(null));
        persist(
                Topics.COMPONENT_REGISTERED,
                component.serialNumber().value(),
                eventId,
                correlationId,
                payload);
    }

    private void persist(
            String topic, String key, UUID eventId, String correlationId, Object payload) {
        OutboxEntity entity = new OutboxEntity();
        entity.id = UUID.randomUUID();
        entity.topic = topic;
        entity.messageKey = key;
        entity.payload = write(payload);
        entity.eventId = eventId;
        entity.correlationId = correlationId;
        entity.status = "PENDING";
        entity.createdAt = Instant.now();
        entity.persist();
    }

    private String write(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Failed to serialize outbox payload", exception);
        }
    }
}
