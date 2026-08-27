package io.traceflow.notification.adapter.in.rest;

import io.traceflow.notification.domain.model.Notification;
import io.traceflow.notification.domain.port.in.ListNotificationsUseCase;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/api/v1/notifications")
@Produces(MediaType.APPLICATION_JSON)
public class NotificationResource {
    private final ListNotificationsUseCase listNotifications;

    public NotificationResource(ListNotificationsUseCase listNotifications) {
        this.listNotifications = listNotifications;
    }

    @GET
    public PageResponse<NotificationResponse> list(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {
        var result = listNotifications.execute(page, size);
        return new PageResponse<>(
                result.items().stream().map(this::toResponse).toList(),
                result.total(),
                result.page(),
                result.size());
    }

    private NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(
                notification.id(),
                notification.inspectionId(),
                notification.serialNumber(),
                notification.message(),
                notification.openedAt(),
                notification.correlationId());
    }
}
