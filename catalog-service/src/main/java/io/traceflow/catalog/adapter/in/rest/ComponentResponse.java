package io.traceflow.catalog.adapter.in.rest;

import java.util.UUID;

public record ComponentResponse(
        UUID id,
        String serialNumber,
        String partNumber,
        UUID plantId,
        String parentSerialNumber,
        String status) {}
