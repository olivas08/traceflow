package io.traceflow.common.kafka;

public final class Topics {
    public static final String COMPONENT_REGISTERED = "component.registered";
    public static final String INSPECTION_COMPLETED = "inspection.completed";
    public static final String INSPECTION_FAILED = "inspection.failed";
    public static final String DLQ = "traceflow.dlq";

    private Topics() {}
}
