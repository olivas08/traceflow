package io.traceflow.catalog.domain.exception;

public final class PlantAlreadyExists extends DomainException {
    public PlantAlreadyExists(String code) {
        super("Plant already exists: " + code);
    }
}
