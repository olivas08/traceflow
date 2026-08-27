package io.traceflow.catalog.adapter.out.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "components")
public class ComponentEntity extends PanacheEntityBase {
    @Id public UUID id;

    @Column(name = "serial_number", nullable = false, unique = true, length = 64)
    public String serialNumber;

    @Column(name = "part_number", nullable = false)
    public String partNumber;

    @Column(name = "plant_id", nullable = false)
    public UUID plantId;

    @Column(name = "parent_serial", length = 64)
    public String parentSerial;

    @Column(nullable = false, length = 32)
    public String status;
}
