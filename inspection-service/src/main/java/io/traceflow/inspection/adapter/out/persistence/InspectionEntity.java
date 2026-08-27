package io.traceflow.inspection.adapter.out.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "inspections")
public class InspectionEntity extends PanacheEntityBase {
    @Id public UUID id;

    @Column(name = "serial_number", nullable = false, length = 64)
    public String serialNumber;

    @Column(nullable = false, length = 8)
    public String result;

    @Column(nullable = false)
    public String inspector;

    @Column(columnDefinition = "TEXT")
    public String notes;

    @Column(name = "inspected_at", nullable = false)
    public Instant inspectedAt;

    @Column(name = "idempotency_key", unique = true)
    public String idempotencyKey;
}
