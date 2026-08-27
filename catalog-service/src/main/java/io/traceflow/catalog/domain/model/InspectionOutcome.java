package io.traceflow.catalog.domain.model;

public enum InspectionOutcome {
    PASS,
    FAIL;

    public static InspectionOutcome from(String raw) {
        if (raw == null) {
            throw new IllegalArgumentException("inspection outcome is required");
        }
        return switch (raw.trim().toUpperCase()) {
            case "PASS" -> PASS;
            case "FAIL" -> FAIL;
            default -> throw new IllegalArgumentException("unknown inspection outcome: " + raw);
        };
    }

    public ComponentStatus toComponentStatus() {
        return this == PASS ? ComponentStatus.PASSED : ComponentStatus.FAILED;
    }
}
