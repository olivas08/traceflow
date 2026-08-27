package io.traceflow.notification.adapter.in.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.reactive.messaging.annotations.Blocking;
import io.traceflow.common.kafka.InspectionFailedEvent;
import io.traceflow.common.kafka.Topics;
import io.traceflow.notification.adapter.out.persistence.ProcessedEventStore;
import io.traceflow.notification.domain.port.in.OpenAlertFromFailedInspectionUseCase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class InspectionFailedConsumer {
    private static final Logger LOG = Logger.getLogger(InspectionFailedConsumer.class);

    private final ObjectMapper objectMapper;
    private final OpenAlertFromFailedInspectionUseCase openAlert;
    private final ProcessedEventStore processedEvents;
    private final Emitter<String> deadLetters;

    public InspectionFailedConsumer(
            ObjectMapper objectMapper,
            OpenAlertFromFailedInspectionUseCase openAlert,
            ProcessedEventStore processedEvents,
            @Channel("dead-letter-out") Emitter<String> deadLetters) {
        this.objectMapper = objectMapper;
        this.openAlert = openAlert;
        this.processedEvents = processedEvents;
        this.deadLetters = deadLetters;
    }

    @Incoming("inspection-failed-in")
    @Blocking
    @Transactional
    public void consume(String payload) {
        InspectionFailedEvent event;
        try {
            event = objectMapper.readValue(payload, InspectionFailedEvent.class);
        } catch (Exception exception) {
            LOG.error("Invalid inspection.failed payload", exception);
            deadLetters.send(payload);
            return;
        }
        String eventId = event.eventId().toString();
        if (processedEvents.alreadyProcessed(eventId)) {
            return;
        }
        try {
            openAlert.execute(
                    new OpenAlertFromFailedInspectionUseCase.Command(
                            event.inspectionId(),
                            event.serialNumber(),
                            event.notes(),
                            event.correlationId()));
            processedEvents.markProcessed(eventId);
        } catch (Exception exception) {
            LOG.errorf(
                    exception, "Failed to open alert for %s, sending to %s", eventId, Topics.DLQ);
            deadLetters.send(payload);
        }
    }
}
