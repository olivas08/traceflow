package io.traceflow.inspection.domain.model;

public enum InspectionResult {
    PASS,
    FAIL;

    public static InspectionResult from(String raw) {
        if (raw == null) {
            throw new IllegalArgumentException("result is required");
        }
        return switch (raw.trim().toUpperCase()) {
            case "PASS" -> PASS;
            case "FAIL" -> FAIL;
            default -> throw new IllegalArgumentException("result must be PASS or FAIL");
        };
    }

    public boolean failed() {
        return this == FAIL;
    }
}
