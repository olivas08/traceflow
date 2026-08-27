package io.traceflow.catalog.application;

import io.traceflow.catalog.domain.model.Component;
import io.traceflow.catalog.domain.model.PageResult;
import io.traceflow.catalog.domain.model.SerialNumber;
import io.traceflow.catalog.domain.model.TimelineEntry;
import io.traceflow.catalog.domain.port.out.ComponentRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

final class InMemoryComponentRepository implements ComponentRepository {
    private final Map<String, Component> bySerial = new LinkedHashMap<>();
    private final Map<String, List<TimelineEntry>> timeline = new LinkedHashMap<>();

    @Override
    public void save(Component component) {
        bySerial.put(component.serialNumber().value(), component);
    }

    @Override
    public boolean existsBySerial(SerialNumber serialNumber) {
        return bySerial.containsKey(serialNumber.value());
    }

    @Override
    public Optional<Component> findBySerial(SerialNumber serialNumber) {
        return Optional.ofNullable(bySerial.get(serialNumber.value()));
    }

    @Override
    public PageResult<Component> findAll(int page, int size) {
        List<Component> all = new ArrayList<>(bySerial.values());
        int from = Math.min(page * size, all.size());
        int to = Math.min(from + size, all.size());
        return new PageResult<>(all.subList(from, to), all.size(), page, size);
    }

    @Override
    public void appendTimeline(SerialNumber serialNumber, TimelineEntry entry) {
        timeline.computeIfAbsent(serialNumber.value(), key -> new ArrayList<>()).add(entry);
    }

    @Override
    public List<TimelineEntry> findTimeline(SerialNumber serialNumber) {
        return List.copyOf(timeline.getOrDefault(serialNumber.value(), List.of()));
    }
}
