package io.traceflow.catalog.domain.model;

import java.util.List;

public record ComponentTrace(Component component, List<TimelineEntry> timeline) {}
