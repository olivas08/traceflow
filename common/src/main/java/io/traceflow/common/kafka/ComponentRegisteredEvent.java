package io.traceflow.common.kafka;

import java.time.Instant;
import java.util.UUID;

public record ComponentRegisteredEvent(
        UUID eventId,
        Instant occurredAt,
        String correlationId,
        String serialNumber,
        String partNumber,
        String plantCode,
        String parentSerialNumber) {}
