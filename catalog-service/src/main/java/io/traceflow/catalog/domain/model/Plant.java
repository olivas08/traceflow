package io.traceflow.catalog.domain.model;

import java.util.UUID;

public final class Plant {
    private final UUID id;
    private final PlantCode code;
    private final String name;
    private final String country;

    public Plant(UUID id, PlantCode code, String name, String country) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.code = code;
        this.name = Require.text(name, "name");
        this.country = Require.text(country, "country");
    }

    public UUID id() {
        return id;
    }

    public PlantCode code() {
        return code;
    }

    public String name() {
        return name;
    }

    public String country() {
        return country;
    }
}
