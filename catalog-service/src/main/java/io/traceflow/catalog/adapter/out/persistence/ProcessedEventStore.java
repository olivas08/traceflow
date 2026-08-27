package io.traceflow.catalog.adapter.out.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import java.time.Instant;

@ApplicationScoped
public class ProcessedEventStore {
    public boolean alreadyProcessed(String eventId) {
        return ProcessedEventEntity.findByIdOptional(eventId).isPresent();
    }

    public void markProcessed(String eventId) {
        ProcessedEventEntity entity = new ProcessedEventEntity();
        entity.eventId = eventId;
        entity.processedAt = Instant.now();
        entity.persist();
    }
}
