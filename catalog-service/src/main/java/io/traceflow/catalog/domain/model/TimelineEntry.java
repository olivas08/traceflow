package io.traceflow.catalog.domain.model;

import java.time.Instant;
import java.util.UUID;

public record TimelineEntry(
        UUID id, Instant occurredAt, String type, String summary, String correlationId) {
    public TimelineEntry {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (occurredAt == null) {
            occurredAt = Instant.now();
        }
        type = Require.text(type, "type");
        summary = Require.text(summary, "summary");
    }
}
