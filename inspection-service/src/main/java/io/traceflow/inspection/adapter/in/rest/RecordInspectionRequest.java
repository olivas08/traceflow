package io.traceflow.inspection.adapter.in.rest;

public record RecordInspectionRequest(
        String serialNumber, String result, String inspector, String notes) {}
