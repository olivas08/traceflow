package io.traceflow.catalog.domain.model;

public record PlantCode(String value) {
    public PlantCode {
        value = Require.text(value, "plant code").toUpperCase();
        if (!value.matches("[A-Z0-9]{2,16}")) {
            throw new IllegalArgumentException("plant code must be 2-16 alphanumeric characters");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
