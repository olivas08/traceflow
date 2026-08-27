package io.traceflow.inspection.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import io.traceflow.inspection.domain.port.in.RecordInspectionUseCase;
import org.junit.jupiter.api.Test;

class RecordInspectionServiceTest {
    @Test
    void failResultPublishesCompletedAndFailedEvents() {
        InMemoryInspectionRepository repository = new InMemoryInspectionRepository();
        RecordingEventPublisher events = new RecordingEventPublisher();
        RecordInspectionService service = new RecordInspectionService(repository, events);

        var inspection =
                service.execute(
                        new RecordInspectionUseCase.Command(
                                "SN-1001", "FAIL", "ada", "crack", "key-1", "corr-1"));

        assertEquals("FAIL", inspection.result().name());
        assertEquals(1, events.completed.size());
        assertEquals(1, events.failed.size());
    }

    @Test
    void idempotencyKeyReturnsSameInspection() {
        InMemoryInspectionRepository repository = new InMemoryInspectionRepository();
        RecordingEventPublisher events = new RecordingEventPublisher();
        RecordInspectionService service = new RecordInspectionService(repository, events);
        RecordInspectionUseCase.Command command =
                new RecordInspectionUseCase.Command(
                        "SN-1001", "PASS", "ada", null, "key-1", "corr-1");

        var first = service.execute(command);
        var second = service.execute(command);

        assertSame(first, second);
        assertEquals(1, events.completed.size());
        assertEquals(0, events.failed.size());
    }
}
