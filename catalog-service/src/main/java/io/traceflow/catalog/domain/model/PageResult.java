package io.traceflow.catalog.domain.model;

import java.util.List;

public record PageResult<T>(List<T> items, long total, int page, int size) {}
