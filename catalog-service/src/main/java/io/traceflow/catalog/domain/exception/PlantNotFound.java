package io.traceflow.catalog.domain.exception;

public final class PlantNotFound extends DomainException {
    public PlantNotFound(String code) {
        super("Plant not found: " + code);
    }
}
