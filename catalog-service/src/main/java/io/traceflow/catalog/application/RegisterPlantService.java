package io.traceflow.catalog.application;

import io.traceflow.catalog.domain.exception.PlantAlreadyExists;
import io.traceflow.catalog.domain.model.Plant;
import io.traceflow.catalog.domain.model.PlantCode;
import io.traceflow.catalog.domain.port.in.RegisterPlantUseCase;
import io.traceflow.catalog.domain.port.out.PlantRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class RegisterPlantService implements RegisterPlantUseCase {
    private final PlantRepository plants;

    public RegisterPlantService(PlantRepository plants) {
        this.plants = plants;
    }

    @Override
    @Transactional
    public Plant execute(Command command) {
        PlantCode code = new PlantCode(command.code());
        if (plants.existsByCode(code)) {
            throw new PlantAlreadyExists(code.value());
        }
        Plant plant = new Plant(null, code, command.name(), command.country());
        plants.save(plant);
        return plant;
    }
}
