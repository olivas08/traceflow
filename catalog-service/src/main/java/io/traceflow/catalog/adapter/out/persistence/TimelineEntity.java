package io.traceflow.catalog.adapter.out.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "component_timeline")
public class TimelineEntity extends PanacheEntityBase {
    @Id public UUID id;

    @Column(name = "serial_number", nullable = false, length = 64)
    public String serialNumber;

    @Column(name = "occurred_at", nullable = false)
    public Instant occurredAt;

    @Column(nullable = false, length = 64)
    public String type;

    @Column(nullable = false)
    public String summary;

    @Column(name = "correlation_id")
    public String correlationId;
}
