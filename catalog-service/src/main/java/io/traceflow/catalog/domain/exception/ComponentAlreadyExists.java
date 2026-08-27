package io.traceflow.catalog.domain.exception;

public final class ComponentAlreadyExists extends DomainException {
    public ComponentAlreadyExists(String serial) {
        super("Component already exists: " + serial);
    }
}
