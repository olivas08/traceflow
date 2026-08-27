package io.traceflow.common.kafka;

import java.time.Instant;
import java.util.UUID;

public record InspectionFailedEvent(
        UUID eventId,
        Instant occurredAt,
        String correlationId,
        UUID inspectionId,
        String serialNumber,
        String inspector,
        String notes) {}
