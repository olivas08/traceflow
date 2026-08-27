# ADR 0005: Common module is integration contracts only

## Status

Accepted

## Context

Sharing a "domain jar" across microservices creates a hidden monolith.

## Decision

`common` contains Kafka topic constants and JSON records (`ComponentRegisteredEvent`, `InspectionCompletedEvent`, `InspectionFailedEvent`, `DeadLetterEvent`). No entities, no use cases, no REST DTOs.

## Consequences

`SerialNumber` is defined in catalog and again in inspection. That duplication is cheaper than a shared kernel.
