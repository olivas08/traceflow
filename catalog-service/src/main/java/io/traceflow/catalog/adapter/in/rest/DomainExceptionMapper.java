package io.traceflow.catalog.adapter.in.rest;

import io.traceflow.catalog.domain.exception.ComponentAlreadyExists;
import io.traceflow.catalog.domain.exception.ComponentNotFound;
import io.traceflow.catalog.domain.exception.DomainException;
import io.traceflow.catalog.domain.exception.PlantAlreadyExists;
import io.traceflow.catalog.domain.exception.PlantNotFound;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DomainExceptionMapper implements ExceptionMapper<DomainException> {
    @Override
    public Response toResponse(DomainException exception) {
        int status =
                switch (exception) {
                    case PlantNotFound ignored -> 404;
                    case ComponentNotFound ignored -> 404;
                    case PlantAlreadyExists ignored -> 409;
                    case ComponentAlreadyExists ignored -> 409;
                    default -> 400;
                };
        return Response.status(status).entity(new ErrorBody(exception.getMessage())).build();
    }

    public record ErrorBody(String message) {}
}
