package io.traceflow.catalog.adapter.in.rest;

import java.util.List;

public record PageResponse<T>(List<T> items, long total, int page, int size) {}
