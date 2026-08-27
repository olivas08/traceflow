package io.traceflow.catalog.adapter.in.rest;

import java.time.Instant;
import java.util.UUID;

public record TimelineResponse(
        UUID id, Instant occurredAt, String type, String summary, String correlationId) {}
