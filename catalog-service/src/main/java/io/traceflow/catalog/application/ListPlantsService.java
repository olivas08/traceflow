package io.traceflow.catalog.application;

import io.traceflow.catalog.domain.model.PageResult;
import io.traceflow.catalog.domain.model.Plant;
import io.traceflow.catalog.domain.port.in.ListPlantsUseCase;
import io.traceflow.catalog.domain.port.out.PlantRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ListPlantsService implements ListPlantsUseCase {
    private final PlantRepository plants;

    public ListPlantsService(PlantRepository plants) {
        this.plants = plants;
    }

    @Override
    @Transactional
    public PageResult<Plant> execute(int page, int size) {
        return plants.findAll(page, size);
    }
}
