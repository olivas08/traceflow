# TraceFlow architecture

TraceFlow is a small event-driven system for industrial component traceability. It is split into three deployable Quarkus services so that each bounded context can fail, scale, and migrate independently.

## Runtime

```
                    ┌─────────────┐
                    │  Swagger UI │
                    └──────┬──────┘
           ┌───────────────┼────────────────┐
           v               v                v
     catalog:8081   inspection:8082  notification:8083
           │               │                ▲
           │ outbox        │ outbox         │ consume
           v               v                │
        PostgreSQL      PostgreSQL      PostgreSQL
        (catalog)       (inspection)    (notification)
                           │
                           v
                         Kafka
              component.registered
              inspection.completed ──► catalog (status + timeline)
              inspection.failed    ──► notification (alert inbox)
              traceflow.dlq
```

## Hexagon (Clean Architecture)

Clean Architecture and hexagonal architecture are the same structure in this repo:

| Clean Architecture | Package |
| --- | --- |
| Entities | `domain.model` |
| Use cases | `domain.port.in` + `application` |
| Interface adapters | `adapter.in.*`, `adapter.out.*` |
| Frameworks | Quarkus, Hibernate, Kafka, Flyway |

Dependencies point inward. ArchUnit enforces that `domain` never imports adapters, JPA, JAX-RS, or Kafka clients.

## Why an outbox

Publishing to Kafka from the HTTP thread after `commit` can lose events if the process dies. Publishing before `commit` can leak events that never persisted. The outbox table is written in the same transaction as the aggregate; a poller publishes pending rows every two seconds.

## Why three databases

Catalog, inspection, and notification do not share tables. That keeps each hexagon free to evolve its schema and matches the "database per service" guideline used in production microservice platforms.

## Correlation

REST adapters read `X-Correlation-Id` or generate a UUID, pass it through the use case, store it on the outbox row, and include it in the Kafka JSON so a failed inspection can be traced from API to alert.
