package io.traceflow.notification.domain.port.out;

import io.traceflow.notification.domain.model.Notification;
import io.traceflow.notification.domain.model.PageResult;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository {
    void save(Notification notification);

    Optional<Notification> findByInspectionId(UUID inspectionId);

    PageResult<Notification> findAll(int page, int size);
}
