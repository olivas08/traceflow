package io.traceflow.common.kafka;

import java.time.Instant;
import java.util.UUID;

public record InspectionCompletedEvent(
        UUID eventId,
        Instant occurredAt,
        String correlationId,
        UUID inspectionId,
        String serialNumber,
        String result,
        String inspector) {}
