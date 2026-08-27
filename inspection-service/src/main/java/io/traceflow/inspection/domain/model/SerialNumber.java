package io.traceflow.inspection.domain.model;

public record SerialNumber(String value) {
    public SerialNumber {
        value = Require.text(value, "serial number").toUpperCase();
    }

    @Override
    public String toString() {
        return value;
    }
}
