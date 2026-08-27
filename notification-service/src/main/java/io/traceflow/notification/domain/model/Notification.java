package io.traceflow.notification.domain.model;

import java.time.Instant;
import java.util.UUID;

public final class Notification {
    private final UUID id;
    private final UUID inspectionId;
    private final String serialNumber;
    private final String message;
    private final Instant openedAt;
    private final String correlationId;

    public Notification(
            UUID id,
            UUID inspectionId,
            String serialNumber,
            String message,
            Instant openedAt,
            String correlationId) {
        this.id = id == null ? UUID.randomUUID() : id;
        if (inspectionId == null) {
            throw new IllegalArgumentException("inspection id is required");
        }
        this.inspectionId = inspectionId;
        this.serialNumber = Require.text(serialNumber, "serial number").toUpperCase();
        this.message = Require.text(message, "message");
        this.openedAt = openedAt == null ? Instant.now() : openedAt;
        this.correlationId = correlationId;
    }

    public static Notification open(
            UUID inspectionId, String serialNumber, String notes, String correlationId) {
        String detail = notes == null || notes.isBlank() ? "no notes" : notes.trim();
        return new Notification(
                UUID.randomUUID(),
                inspectionId,
                serialNumber,
                "Inspection failed for " + serialNumber.toUpperCase() + ": " + detail,
                Instant.now(),
                correlationId);
    }

    public UUID id() {
        return id;
    }

    public UUID inspectionId() {
        return inspectionId;
    }

    public String serialNumber() {
        return serialNumber;
    }

    public String message() {
        return message;
    }

    public Instant openedAt() {
        return openedAt;
    }

    public String correlationId() {
        return correlationId;
    }
}
