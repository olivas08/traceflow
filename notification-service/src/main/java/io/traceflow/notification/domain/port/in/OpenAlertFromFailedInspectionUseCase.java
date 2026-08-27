package io.traceflow.notification.domain.port.in;

import io.traceflow.notification.domain.model.Notification;
import java.util.UUID;

public interface OpenAlertFromFailedInspectionUseCase {
    Notification execute(Command command);

    record Command(UUID inspectionId, String serialNumber, String notes, String correlationId) {}
}
