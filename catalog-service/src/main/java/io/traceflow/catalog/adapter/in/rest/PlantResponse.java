package io.traceflow.catalog.adapter.in.rest;

import java.util.UUID;

public record PlantResponse(UUID id, String code, String name, String country) {}
