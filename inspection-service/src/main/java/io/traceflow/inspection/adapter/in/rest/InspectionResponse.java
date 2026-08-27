package io.traceflow.inspection.adapter.in.rest;

import java.time.Instant;
import java.util.UUID;

public record InspectionResponse(
        UUID id,
        String serialNumber,
        String result,
        String inspector,
        String notes,
        Instant inspectedAt) {}
