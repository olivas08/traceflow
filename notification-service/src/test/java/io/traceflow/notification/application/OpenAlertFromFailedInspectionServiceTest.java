package io.traceflow.notification.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import io.traceflow.notification.domain.port.in.OpenAlertFromFailedInspectionUseCase;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class OpenAlertFromFailedInspectionServiceTest {
    @Test
    void opensAlertOncePerInspection() {
        InMemoryNotificationRepository repository = new InMemoryNotificationRepository();
        OpenAlertFromFailedInspectionService service =
                new OpenAlertFromFailedInspectionService(repository);
        UUID inspectionId = UUID.randomUUID();
        OpenAlertFromFailedInspectionUseCase.Command command =
                new OpenAlertFromFailedInspectionUseCase.Command(
                        inspectionId, "SN-1001", "crack", "corr-1");

        var first = service.execute(command);
        var second = service.execute(command);

        assertSame(first, second);
        assertEquals(1, repository.findAll(0, 20).total());
        assertEquals("SN-1001", first.serialNumber());
    }
}
