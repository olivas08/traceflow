package io.traceflow.notification.domain.port.in;

import io.traceflow.notification.domain.model.Notification;
import io.traceflow.notification.domain.model.PageResult;

public interface ListNotificationsUseCase {
    PageResult<Notification> execute(int page, int size);
}
