package io.traceflow.catalog.adapter.out.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "processed_events")
public class ProcessedEventEntity extends PanacheEntityBase {
    @Id
    @Column(name = "event_id")
    public String eventId;

    @Column(name = "processed_at", nullable = false)
    public Instant processedAt;
}
