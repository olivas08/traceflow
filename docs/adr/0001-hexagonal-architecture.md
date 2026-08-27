# ADR 0001: Hexagonal / Clean Architecture

## Status

Accepted

## Context

TraceFlow is a portfolio project meant to show production-style backend design, not a tutorial CRUD app. The code must stay readable in an interview: small classes, obvious names, and a domain that can be tested without Docker.

## Decision

Each service is a hexagon:

- Domain: model, inbound/outbound ports, domain exceptions. Pure Java.
- Application: one class per use case, one public `execute` method.
- Adapters: REST, Kafka, JPA, outbox.

Clean Architecture layers map 1:1 to those packages. We do **not** add a second folder tree, a mediator, MapStruct, or Lombok.

SOLID is applied concretely:

- SRP: no `ComponentService` god class
- OCP: a new delivery mechanism is a new adapter
- LSP: in-memory fakes replace JPA in unit tests
- ISP: one inbound port per use case
- DIP: application depends on repository interfaces, not Panache

ArchUnit fails the build if the rules are broken.

## Consequences

More files than a layered "controller-service-repository" demo. Each file is short. Recruiters can open a use case and read it top to bottom.
