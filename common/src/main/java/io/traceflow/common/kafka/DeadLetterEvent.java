package io.traceflow.common.kafka;

import java.time.Instant;
import java.util.UUID;

public record DeadLetterEvent(
        UUID eventId,
        Instant occurredAt,
        String originalTopic,
        String originalKey,
        String reason,
        String payload) {}
