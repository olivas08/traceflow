package io.traceflow.catalog.adapter.out.persistence;

import io.traceflow.catalog.domain.model.PageResult;
import io.traceflow.catalog.domain.model.Plant;
import io.traceflow.catalog.domain.model.PlantCode;
import io.traceflow.catalog.domain.port.out.PlantRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class JpaPlantRepository implements PlantRepository {
    @Override
    public void save(Plant plant) {
        PlantEntity entity = CatalogPersistenceMapper.toEntity(plant);
        PlantEntity.getEntityManager().merge(entity);
    }

    @Override
    public boolean existsByCode(PlantCode code) {
        return PlantEntity.count("code", code.value()) > 0;
    }

    @Override
    public Optional<Plant> findByCode(PlantCode code) {
        return PlantEntity.find("code", code.value())
                .firstResultOptional()
                .map(entity -> CatalogPersistenceMapper.toDomain((PlantEntity) entity));
    }

    @Override
    public Optional<Plant> findById(UUID id) {
        return PlantEntity.findByIdOptional(id)
                .map(entity -> CatalogPersistenceMapper.toDomain((PlantEntity) entity));
    }

    @Override
    public PageResult<Plant> findAll(int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = size < 1 ? 20 : Math.min(size, 100);
        List<Plant> items =
                PlantEntity.findAll().page(safePage, safeSize).list().stream()
                        .map(entity -> CatalogPersistenceMapper.toDomain((PlantEntity) entity))
                        .toList();
        return new PageResult<>(items, PlantEntity.count(), safePage, safeSize);
    }
}
