package io.traceflow.catalog.domain.model;

import java.util.Optional;
import java.util.UUID;

public final class Component {
    private final UUID id;
    private final SerialNumber serialNumber;
    private final String partNumber;
    private final UUID plantId;
    private final SerialNumber parentSerial;
    private ComponentStatus status;

    public Component(
            UUID id,
            SerialNumber serialNumber,
            String partNumber,
            UUID plantId,
            SerialNumber parentSerial,
            ComponentStatus status) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.serialNumber = serialNumber;
        this.partNumber = Require.text(partNumber, "part number");
        if (plantId == null) {
            throw new IllegalArgumentException("plant id is required");
        }
        this.plantId = plantId;
        this.parentSerial = parentSerial;
        this.status = status == null ? ComponentStatus.REGISTERED : status;
    }

    public static Component registered(
            SerialNumber serialNumber, String partNumber, UUID plantId, SerialNumber parentSerial) {
        return new Component(
                UUID.randomUUID(),
                serialNumber,
                partNumber,
                plantId,
                parentSerial,
                ComponentStatus.REGISTERED);
    }

    public void applyInspection(InspectionOutcome outcome) {
        this.status = outcome.toComponentStatus();
    }

    public UUID id() {
        return id;
    }

    public SerialNumber serialNumber() {
        return serialNumber;
    }

    public String partNumber() {
        return partNumber;
    }

    public UUID plantId() {
        return plantId;
    }

    public Optional<SerialNumber> parentSerial() {
        return Optional.ofNullable(parentSerial);
    }

    public ComponentStatus status() {
        return status;
    }
}
