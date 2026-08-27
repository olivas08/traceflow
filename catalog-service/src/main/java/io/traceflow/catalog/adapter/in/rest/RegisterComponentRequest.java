package io.traceflow.catalog.adapter.in.rest;

public record RegisterComponentRequest(
        String plantCode, String serialNumber, String partNumber, String parentSerialNumber) {}
