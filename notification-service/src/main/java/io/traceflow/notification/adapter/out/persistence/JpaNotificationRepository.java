package io.traceflow.notification.adapter.out.persistence;

import io.traceflow.notification.domain.model.Notification;
import io.traceflow.notification.domain.model.PageResult;
import io.traceflow.notification.domain.port.out.NotificationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class JpaNotificationRepository implements NotificationRepository {
    @Override
    public void save(Notification notification) {
        NotificationEntity entity = new NotificationEntity();
        entity.id = notification.id();
        entity.inspectionId = notification.inspectionId();
        entity.serialNumber = notification.serialNumber();
        entity.message = notification.message();
        entity.openedAt = notification.openedAt();
        entity.correlationId = notification.correlationId();
        NotificationEntity.getEntityManager().merge(entity);
    }

    @Override
    public Optional<Notification> findByInspectionId(UUID inspectionId) {
        return NotificationEntity.find("inspectionId", inspectionId)
                .firstResultOptional()
                .map(entity -> toDomain((NotificationEntity) entity));
    }

    @Override
    public PageResult<Notification> findAll(int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = size < 1 ? 20 : Math.min(size, 100);
        List<Notification> items =
                NotificationEntity.find("order by openedAt desc")
                        .page(safePage, safeSize)
                        .list()
                        .stream()
                        .map(entity -> toDomain((NotificationEntity) entity))
                        .toList();
        return new PageResult<>(items, NotificationEntity.count(), safePage, safeSize);
    }

    private Notification toDomain(NotificationEntity entity) {
        return new Notification(
                entity.id,
                entity.inspectionId,
                entity.serialNumber,
                entity.message,
                entity.openedAt,
                entity.correlationId);
    }
}
