package io.traceflow.notification.application;

import io.traceflow.notification.domain.model.Notification;
import io.traceflow.notification.domain.port.in.OpenAlertFromFailedInspectionUseCase;
import io.traceflow.notification.domain.port.out.NotificationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class OpenAlertFromFailedInspectionService implements OpenAlertFromFailedInspectionUseCase {
    private final NotificationRepository notifications;

    public OpenAlertFromFailedInspectionService(NotificationRepository notifications) {
        this.notifications = notifications;
    }

    @Override
    @Transactional
    public Notification execute(Command command) {
        var existing = notifications.findByInspectionId(command.inspectionId());
        if (existing.isPresent()) {
            return existing.get();
        }
        Notification notification =
                Notification.open(
                        command.inspectionId(),
                        command.serialNumber(),
                        command.notes(),
                        command.correlationId());
        notifications.save(notification);
        return notification;
    }
}
