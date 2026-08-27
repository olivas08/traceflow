# TraceFlow

Event-driven manufacturing traceability platform. Three Quarkus services record plants and components, apply shop-floor inspections, and open alerts when an inspection fails. Kafka carries domain events; each service owns its PostgreSQL schema.

This repository is a portfolio slice of cloud-native backend work: **Clean Architecture / hexagonal**, **SOLID**, transactional **outbox**, **idempotent consumers**, Docker Compose, Kubernetes manifests, and GitHub Actions.

## Architecture

```
Client / Swagger UI
        | REST
        v
 catalog-service (8081)     inspection-service (8082)     notification-service (8083)
        | outbox                    | outbox                       ^
        v                           v                              |
     Kafka  <--- inspection.completed / inspection.failed ---------+
        |
        +--> catalog updates component status + timeline
        +--> notification opens an alert (FAIL only)
```

Each service is a hexagon:

- `domain` — pure Java model, ports, exceptions
- `application` — one use case class, one `execute` method
- `adapter.in` — JAX-RS and Kafka consumers
- `adapter.out` — JPA/Panache, Flyway, outbox publisher

See [docs/architecture.md](docs/architecture.md) and [docs/adr](docs/adr).

## Stack

| Layer | Choice |
| --- | --- |
| Language | Java 21 |
| Framework | Quarkus 3.27 LTS |
| Messaging | Apache Kafka (KRaft) |
| Database | PostgreSQL 16 (one database per service) |
| Local run | Docker Compose |
| Kubernetes | `kind` manifests in `infra/k8s` |
| CI | GitHub Actions (`./mvnw verify`, Jib images to GHCR) |

Everything in this MVP is free and open source. No cloud account is required.

## Prerequisites

- JDK 21
- Docker (for Compose and for tests that start PostgreSQL + Kafka via Quarkus Dev Services)
- Maven Wrapper is included (`./mvnw`). On Windows PowerShell use `mvnw.cmd`.

## Quick start (Docker Compose)

```bash
docker compose up --build
```

| Service | URL |
| --- | --- |
| Catalog Swagger | http://localhost:8081/q/swagger-ui |
| Inspection Swagger | http://localhost:8082/q/swagger-ui |
| Notification Swagger | http://localhost:8083/q/swagger-ui |
| Kafka UI | http://localhost:8090 |
| Health | `http://localhost:8081/q/health` |

### Demo flow

```bash
# 1. Register a plant
curl -s -X POST http://localhost:8081/api/v1/plants \
  -H 'Content-Type: application/json' \
  -d '{"code":"PT01","name":"Porto Plant","country":"PT"}'

# 2. Register a component
curl -s -X POST http://localhost:8081/api/v1/components \
  -H 'Content-Type: application/json' \
  -H 'X-Correlation-Id: demo-1' \
  -d '{"plantCode":"PT01","serialNumber":"SN-1001","partNumber":"BRAKE-DISC"}'

# 3. Fail an inspection (idempotent if you replay the same Idempotency-Key)
curl -s -X POST http://localhost:8082/api/v1/inspections \
  -H 'Content-Type: application/json' \
  -H 'Idempotency-Key: inspect-1' \
  -H 'X-Correlation-Id: demo-1' \
  -d '{"serialNumber":"SN-1001","result":"FAIL","inspector":"ada","notes":"crack on rim"}'

# 4. Wait a couple of seconds for the outbox poller, then read the trace
curl -s http://localhost:8081/api/v1/components/SN-1001/trace

# 5. Read the alert inbox
curl -s http://localhost:8083/api/v1/notifications
```

Expected: component status `FAILED`, a timeline with registration + inspection, and one notification.

## Run tests

```bash
./mvnw verify
```

- Domain/application tests use in-memory fakes (no Docker).
- `@QuarkusTest` REST tests use H2 and in-memory Kafka channels so `./mvnw verify` works without Docker.
- ArchUnit fails the build if `domain` or `application` depend on adapters or persistence/messaging frameworks.
- PostgreSQL + Kafka are exercised through Docker Compose (and GitHub Actions image builds).

## Kubernetes (kind)

```bash
kind create cluster --name traceflow
docker compose build
# Tag/load images into kind, then:
kubectl apply -f infra/k8s
```

Manifests expect images such as `ghcr.io/<owner>/catalog-service:1.0.0` after the `images` workflow publishes to GHCR. For a local kind cluster, retag the Compose images to match the Deployment specs (or edit the image fields).

```bash
kubectl -n traceflow get pods
kubectl -n traceflow port-forward svc/catalog-service 8081:8081
```

## API summary

| Service | Method | Path |
| --- | --- | --- |
| catalog | POST/GET | `/api/v1/plants` |
| catalog | POST/GET | `/api/v1/components` |
| catalog | GET | `/api/v1/components/{serial}` |
| catalog | GET | `/api/v1/components/{serial}/trace` |
| inspection | POST/GET | `/api/v1/inspections` |
| inspection | GET | `/api/v1/inspections/{id}` |
| notification | GET | `/api/v1/notifications` |

Headers: `X-Correlation-Id` (generated if omitted), `Idempotency-Key` on `POST /inspections`.

Kafka topics: `component.registered`, `inspection.completed`, `inspection.failed`, `traceflow.dlq`.

## Architecture and code guidelines

- One use case = one class = one public `execute` method.
- Domain objects have no Quarkus, JPA, or Kafka imports.
- JPA entities live in adapters and map to domain objects by hand (no MapStruct, no Lombok).
- Writes go to an outbox table in the same transaction; a scheduled poller publishes to Kafka.
- Consumers skip events they already processed and send poison payloads to `traceflow.dlq`.

## Project layout

```
common/                 Kafka JSON contracts only
catalog-service/
inspection-service/
notification-service/
infra/docker/           Dockerfile + Postgres init
infra/k8s/              kind/AKS-ready manifests
docs/adr/               Architecture decision records
```

## License

Use this project as a public portfolio sample. No proprietary BMW or insurance data is included.
