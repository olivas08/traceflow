package io.traceflow.catalog.adapter.out.messaging;

import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;
import io.traceflow.catalog.adapter.out.persistence.OutboxEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;

@ApplicationScoped
public class OutboxPoller {
    private static final int BATCH_SIZE = 50;

    @Inject
    @Channel("outbox-out")
    Emitter<String> emitter;

    @Transactional
    public void publishPending() {
        List<OutboxEntity> pending =
                OutboxEntity.find("status = ?1 order by createdAt asc", "PENDING")
                        .page(0, BATCH_SIZE)
                        .list();
        for (OutboxEntity event : pending) {
            emitter.send(
                    Message.of(event.payload)
                            .addMetadata(
                                    OutgoingKafkaRecordMetadata.<String>builder()
                                            .withTopic(event.topic)
                                            .withKey(event.messageKey)
                                            .build()));
            event.status = "PUBLISHED";
            event.publishedAt = Instant.now();
        }
    }
}
