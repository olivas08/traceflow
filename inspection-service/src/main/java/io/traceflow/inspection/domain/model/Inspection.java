package io.traceflow.inspection.domain.model;

import java.time.Instant;
import java.util.UUID;

public final class Inspection {
    private final UUID id;
    private final SerialNumber serialNumber;
    private final InspectionResult result;
    private final String inspector;
    private final String notes;
    private final Instant inspectedAt;
    private final String idempotencyKey;

    public Inspection(
            UUID id,
            SerialNumber serialNumber,
            InspectionResult result,
            String inspector,
            String notes,
            Instant inspectedAt,
            String idempotencyKey) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.serialNumber = serialNumber;
        this.result = result;
        this.inspector = Require.text(inspector, "inspector");
        this.notes = notes == null ? "" : notes.trim();
        this.inspectedAt = inspectedAt == null ? Instant.now() : inspectedAt;
        this.idempotencyKey = idempotencyKey;
    }

    public static Inspection record(
            SerialNumber serialNumber,
            InspectionResult result,
            String inspector,
            String notes,
            String idempotencyKey) {
        return new Inspection(
                UUID.randomUUID(),
                serialNumber,
                result,
                inspector,
                notes,
                Instant.now(),
                idempotencyKey);
    }

    public UUID id() {
        return id;
    }

    public SerialNumber serialNumber() {
        return serialNumber;
    }

    public InspectionResult result() {
        return result;
    }

    public String inspector() {
        return inspector;
    }

    public String notes() {
        return notes;
    }

    public Instant inspectedAt() {
        return inspectedAt;
    }

    public String idempotencyKey() {
        return idempotencyKey;
    }
}
