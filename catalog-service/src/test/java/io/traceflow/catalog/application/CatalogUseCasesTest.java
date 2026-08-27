package io.traceflow.catalog.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.traceflow.catalog.domain.exception.ComponentAlreadyExists;
import io.traceflow.catalog.domain.exception.PlantNotFound;
import io.traceflow.catalog.domain.model.ComponentStatus;
import io.traceflow.catalog.domain.model.Plant;
import io.traceflow.catalog.domain.model.PlantCode;
import io.traceflow.catalog.domain.port.in.ApplyInspectionResultUseCase;
import io.traceflow.catalog.domain.port.in.GetTraceUseCase;
import io.traceflow.catalog.domain.port.in.RegisterComponentUseCase;
import io.traceflow.catalog.domain.port.in.RegisterPlantUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CatalogUseCasesTest {
    private InMemoryPlantRepository plants;
    private InMemoryComponentRepository components;
    private RecordingEventPublisher events;
    private RegisterPlantService registerPlant;
    private RegisterComponentService registerComponent;
    private GetTraceService getTrace;
    private ApplyInspectionResultService applyInspection;

    @BeforeEach
    void setUp() {
        plants = new InMemoryPlantRepository();
        components = new InMemoryComponentRepository();
        events = new RecordingEventPublisher();
        registerPlant = new RegisterPlantService(plants);
        registerComponent = new RegisterComponentService(plants, components, events);
        getTrace = new GetTraceService(components);
        applyInspection = new ApplyInspectionResultService(components);
    }

    @Test
    void registersPlantAndComponentThenAppliesFailedInspection() {
        Plant plant =
                registerPlant.execute(
                        new RegisterPlantUseCase.Command("PT01", "Porto Plant", "PT"));
        var component =
                registerComponent.execute(
                        new RegisterComponentUseCase.Command(
                                plant.code().value(), "SN-1001", "BRAKE-DISC", null, "corr-1"));

        applyInspection.execute(
                new ApplyInspectionResultUseCase.Command(
                        component.serialNumber().value(),
                        "FAIL",
                        "inspector.ada",
                        "corr-2",
                        "evt-1"));

        var trace = getTrace.execute("SN-1001");
        assertEquals(ComponentStatus.FAILED, trace.component().status());
        assertEquals(2, trace.timeline().size());
        assertEquals(1, events.serials.size());
        assertTrue(plants.existsByCode(new PlantCode("PT01")));
    }

    @Test
    void rejectsUnknownPlant() {
        assertThrows(
                PlantNotFound.class,
                () ->
                        registerComponent.execute(
                                new RegisterComponentUseCase.Command(
                                        "NOPE", "SN-1001", "BRAKE-DISC", null, "corr-1")));
    }

    @Test
    void rejectsDuplicateSerial() {
        registerPlant.execute(new RegisterPlantUseCase.Command("PT01", "Porto Plant", "PT"));
        RegisterComponentUseCase.Command command =
                new RegisterComponentUseCase.Command(
                        "PT01", "SN-1001", "BRAKE-DISC", null, "corr-1");
        registerComponent.execute(command);
        assertThrows(ComponentAlreadyExists.class, () -> registerComponent.execute(command));
    }

    @Test
    void getTraceFailsWhenComponentMissing() {
        GetTraceUseCase useCase = getTrace;
        assertThrows(RuntimeException.class, () -> useCase.execute("SN-MISSING"));
    }
}
