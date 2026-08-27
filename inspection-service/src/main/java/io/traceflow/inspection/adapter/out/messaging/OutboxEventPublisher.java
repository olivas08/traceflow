package io.traceflow.inspection.adapter.out.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.traceflow.common.kafka.InspectionCompletedEvent;
import io.traceflow.common.kafka.InspectionFailedEvent;
import io.traceflow.common.kafka.Topics;
import io.traceflow.inspection.adapter.out.persistence.OutboxEntity;
import io.traceflow.inspection.domain.model.Inspection;
import io.traceflow.inspection.domain.port.out.DomainEventPublisher;
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
    public void publishCompleted(Inspection inspection, String correlationId) {
        UUID eventId = UUID.randomUUID();
        persist(
                Topics.INSPECTION_COMPLETED,
                inspection.serialNumber().value(),
                eventId,
                correlationId,
                new InspectionCompletedEvent(
                        eventId,
                        inspection.inspectedAt(),
                        correlationId,
                        inspection.id(),
                        inspection.serialNumber().value(),
                        inspection.result().name(),
                        inspection.inspector()));
    }

    @Override
    public void publishFailed(Inspection inspection, String correlationId) {
        UUID eventId = UUID.randomUUID();
        persist(
                Topics.INSPECTION_FAILED,
                inspection.serialNumber().value(),
                eventId,
                correlationId,
                new InspectionFailedEvent(
                        eventId,
                        inspection.inspectedAt(),
                        correlationId,
                        inspection.id(),
                        inspection.serialNumber().value(),
                        inspection.inspector(),
                        inspection.notes()));
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
