# ADR 0002: Transactional outbox

## Status

Accepted

## Context

Catalog and inspection must persist state and notify other services. Dual-write (DB + Kafka in two steps) is a common source of missing or duplicate events.

## Decision

Use cases call `DomainEventPublisher`. The adapter implementation inserts a row into `outbox_events` in the same database transaction as the aggregate. A scheduled poller (`every = 2s`) publishes `PENDING` rows to Kafka and marks them `PUBLISHED`.

The domain does not know Kafka topic names. Those live in `common` and in the outbox adapter.

## Consequences

Events appear with a short delay. That is acceptable for this domain (shop-floor traceability, not sub-millisecond trading). At-least-once delivery is handled by idempotent consumers.
