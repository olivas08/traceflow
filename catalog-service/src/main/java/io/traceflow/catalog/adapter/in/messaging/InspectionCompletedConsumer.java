package io.traceflow.catalog.adapter.in.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.reactive.messaging.annotations.Blocking;
import io.traceflow.catalog.adapter.out.persistence.ProcessedEventStore;
import io.traceflow.catalog.domain.port.in.ApplyInspectionResultUseCase;
import io.traceflow.common.kafka.InspectionCompletedEvent;
import io.traceflow.common.kafka.Topics;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class InspectionCompletedConsumer {
    private static final Logger LOG = Logger.getLogger(InspectionCompletedConsumer.class);

    private final ObjectMapper objectMapper;
    private final ApplyInspectionResultUseCase applyInspection;
    private final ProcessedEventStore processedEvents;
    private final Emitter<String> deadLetters;

    public InspectionCompletedConsumer(
            ObjectMapper objectMapper,
            ApplyInspectionResultUseCase applyInspection,
            ProcessedEventStore processedEvents,
            @Channel("dead-letter-out") Emitter<String> deadLetters) {
        this.objectMapper = objectMapper;
        this.applyInspection = applyInspection;
        this.processedEvents = processedEvents;
        this.deadLetters = deadLetters;
    }

    @Incoming("inspection-completed-in")
    @Blocking
    @Transactional
    public void consume(String payload) {
        InspectionCompletedEvent event;
        try {
            event = objectMapper.readValue(payload, InspectionCompletedEvent.class);
        } catch (Exception exception) {
            LOG.error("Invalid inspection.completed payload", exception);
            deadLetters.send(payload);
            return;
        }
        String eventId = event.eventId().toString();
        if (processedEvents.alreadyProcessed(eventId)) {
            return;
        }
        try {
            applyInspection.execute(
                    new ApplyInspectionResultUseCase.Command(
                            event.serialNumber(),
                            event.result(),
                            event.inspector(),
                            event.correlationId(),
                            eventId));
            processedEvents.markProcessed(eventId);
        } catch (Exception exception) {
            LOG.errorf(
                    exception, "Failed to apply inspection %s, sending to %s", eventId, Topics.DLQ);
            deadLetters.send(payload);
        }
    }
}
