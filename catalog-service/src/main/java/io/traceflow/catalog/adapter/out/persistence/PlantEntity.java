package io.traceflow.catalog.adapter.out.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "plants")
public class PlantEntity extends PanacheEntityBase {
    @Id public UUID id;

    @Column(nullable = false, unique = true, length = 16)
    public String code;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false, length = 64)
    public String country;
}
