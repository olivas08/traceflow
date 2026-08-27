# ADR 0004: Database per service

## Status

Accepted

## Context

A shared schema would make the demo smaller. It would also hide the main reason to split services.

## Decision

One PostgreSQL instance locally, three databases: `catalog`, `inspection`, `notification`. Flyway migrations live inside each service. No cross-database foreign keys.

## Consequences

Docker Compose init creates the three databases. Kubernetes uses the same layout. Joining component + inspection data is done through events and the catalog timeline projection, not SQL joins.
