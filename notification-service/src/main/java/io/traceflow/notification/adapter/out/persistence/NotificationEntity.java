package io.traceflow.notification.adapter.out.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notifications")
public class NotificationEntity extends PanacheEntityBase {
    @Id public UUID id;

    @Column(name = "inspection_id", nullable = false, unique = true)
    public UUID inspectionId;

    @Column(name = "serial_number", nullable = false, length = 64)
    public String serialNumber;

    @Column(nullable = false)
    public String message;

    @Column(name = "opened_at", nullable = false)
    public Instant openedAt;

    @Column(name = "correlation_id")
    public String correlationId;
}
