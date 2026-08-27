package io.traceflow.catalog.adapter.out.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
public class OutboxEntity extends PanacheEntityBase {
    @Id public UUID id;

    @Column(nullable = false)
    public String topic;

    @Column(name = "message_key", nullable = false)
    public String messageKey;

    @Column(nullable = false, columnDefinition = "TEXT")
    public String payload;

    @Column(name = "event_id", nullable = false, unique = true)
    public UUID eventId;

    @Column(name = "correlation_id")
    public String correlationId;

    @Column(nullable = false, length = 16)
    public String status;

    @Column(name = "created_at", nullable = false)
    public Instant createdAt;

    @Column(name = "published_at")
    public Instant publishedAt;
}
