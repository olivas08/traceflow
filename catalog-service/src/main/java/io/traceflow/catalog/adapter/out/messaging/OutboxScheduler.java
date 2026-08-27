package io.traceflow.catalog.adapter.out.messaging;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OutboxScheduler {
    private final OutboxPoller poller;

    public OutboxScheduler(OutboxPoller poller) {
        this.poller = poller;
    }

    @Scheduled(every = "2s", identity = "catalog-outbox")
    void tick() {
        poller.publishPending();
    }
}
