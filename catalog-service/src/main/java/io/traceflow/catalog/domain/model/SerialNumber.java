package io.traceflow.catalog.domain.model;

public record SerialNumber(String value) {
    public SerialNumber {
        value = Require.text(value, "serial number").toUpperCase();
        if (value.length() < 4 || value.length() > 64) {
            throw new IllegalArgumentException("serial number must be 4-64 characters");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
