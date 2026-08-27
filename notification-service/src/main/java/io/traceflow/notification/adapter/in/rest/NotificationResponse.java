package io.traceflow.notification.adapter.in.rest;

import java.time.Instant;
import java.util.UUID;

public record NotificationResponse(
        UUID id,
        UUID inspectionId,
        String serialNumber,
        String message,
        Instant openedAt,
        String correlationId) {}
