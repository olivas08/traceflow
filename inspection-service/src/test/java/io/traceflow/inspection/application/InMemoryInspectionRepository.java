package io.traceflow.inspection.application;

import io.traceflow.inspection.domain.model.Inspection;
import io.traceflow.inspection.domain.model.PageResult;
import io.traceflow.inspection.domain.port.out.InspectionRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

final class InMemoryInspectionRepository implements InspectionRepository {
    private final Map<UUID, Inspection> byId = new LinkedHashMap<>();

    @Override
    public void save(Inspection inspection) {
        byId.put(inspection.id(), inspection);
    }

    @Override
    public Optional<Inspection> findById(UUID id) {
        return Optional.ofNullable(byId.get(id));
    }

    @Override
    public Optional<Inspection> findByIdempotencyKey(String key) {
        return byId.values().stream()
                .filter(inspection -> key.equals(inspection.idempotencyKey()))
                .findFirst();
    }

    @Override
    public PageResult<Inspection> findAll(int page, int size) {
        List<Inspection> all = new ArrayList<>(byId.values());
        int from = Math.min(page * size, all.size());
        int to = Math.min(from + size, all.size());
        return new PageResult<>(all.subList(from, to), all.size(), page, size);
    }
}
