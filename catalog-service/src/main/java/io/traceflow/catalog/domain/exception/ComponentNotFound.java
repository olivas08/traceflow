package io.traceflow.catalog.domain.exception;

public final class ComponentNotFound extends DomainException {
    public ComponentNotFound(String serial) {
        super("Component not found: " + serial);
    }
}
