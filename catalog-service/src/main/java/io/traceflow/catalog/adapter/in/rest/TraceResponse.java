package io.traceflow.catalog.adapter.in.rest;

import java.util.List;

public record TraceResponse(ComponentResponse component, List<TimelineResponse> timeline) {}
