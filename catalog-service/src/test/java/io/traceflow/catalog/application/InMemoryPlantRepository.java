package io.traceflow.catalog.application;

import io.traceflow.catalog.domain.model.PageResult;
import io.traceflow.catalog.domain.model.Plant;
import io.traceflow.catalog.domain.model.PlantCode;
import io.traceflow.catalog.domain.port.out.PlantRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

final class InMemoryPlantRepository implements PlantRepository {
    private final Map<UUID, Plant> byId = new LinkedHashMap<>();

    @Override
    public void save(Plant plant) {
        byId.put(plant.id(), plant);
    }

    @Override
    public boolean existsByCode(PlantCode code) {
        return findByCode(code).isPresent();
    }

    @Override
    public Optional<Plant> findByCode(PlantCode code) {
        return byId.values().stream().filter(plant -> plant.code().equals(code)).findFirst();
    }

    @Override
    public Optional<Plant> findById(UUID id) {
        return Optional.ofNullable(byId.get(id));
    }

    @Override
    public PageResult<Plant> findAll(int page, int size) {
        List<Plant> all = new ArrayList<>(byId.values());
        int from = Math.min(page * size, all.size());
        int to = Math.min(from + size, all.size());
        return new PageResult<>(all.subList(from, to), all.size(), page, size);
    }
}
