package io.traceflow.catalog.domain.model;

public final class Require {
    private Require() {}

    public static String text(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
        return value.trim();
    }
}
