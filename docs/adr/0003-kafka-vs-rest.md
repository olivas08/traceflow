# ADR 0003: Kafka for cross-service facts, REST for commands

## Status

Accepted

## Context

We could let inspection call catalog over HTTP after recording a result. That couples availability and turns a shop-floor write into a distributed transaction.

## Decision

- **REST** is for commands and queries owned by one service (register a plant, record an inspection, list alerts).
- **Kafka** is for facts other services need to project (`inspection.completed`, `inspection.failed`).

`common` holds only JSON contracts and topic names. Each service maps those contracts into its own domain.

Invalid or repeatedly failing payloads go to `traceflow.dlq`.

## Consequences

Catalog can be briefly down while inspections still persist. The projection catches up when catalog is healthy. Users of the demo should wait a couple of seconds after POST inspection before GET trace.
