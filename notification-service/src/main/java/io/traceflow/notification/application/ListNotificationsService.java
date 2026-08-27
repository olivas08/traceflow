package io.traceflow.notification.application;

import io.traceflow.notification.domain.model.Notification;
import io.traceflow.notification.domain.model.PageResult;
import io.traceflow.notification.domain.port.in.ListNotificationsUseCase;
import io.traceflow.notification.domain.port.out.NotificationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ListNotificationsService implements ListNotificationsUseCase {
    private final NotificationRepository notifications;

    public ListNotificationsService(NotificationRepository notifications) {
        this.notifications = notifications;
    }

    @Override
    @Transactional
    public PageResult<Notification> execute(int page, int size) {
        return notifications.findAll(page, size);
    }
}
