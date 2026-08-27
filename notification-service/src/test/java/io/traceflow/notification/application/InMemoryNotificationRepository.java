package io.traceflow.notification.application;

import io.traceflow.notification.domain.model.Notification;
import io.traceflow.notification.domain.model.PageResult;
import io.traceflow.notification.domain.port.out.NotificationRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

final class InMemoryNotificationRepository implements NotificationRepository {
    private final Map<UUID, Notification> byInspection = new LinkedHashMap<>();

    @Override
    public void save(Notification notification) {
        byInspection.put(notification.inspectionId(), notification);
    }

    @Override
    public Optional<Notification> findByInspectionId(UUID inspectionId) {
        return Optional.ofNullable(byInspection.get(inspectionId));
    }

    @Override
    public PageResult<Notification> findAll(int page, int size) {
        List<Notification> all = new ArrayList<>(byInspection.values());
        int from = Math.min(page * size, all.size());
        int to = Math.min(from + size, all.size());
        return new PageResult<>(all.subList(from, to), all.size(), page, size);
    }
}
