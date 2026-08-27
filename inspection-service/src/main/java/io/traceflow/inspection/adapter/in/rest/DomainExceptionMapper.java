package io.traceflow.inspection.adapter.in.rest;

import io.traceflow.inspection.domain.exception.DomainException;
import io.traceflow.inspection.domain.exception.InspectionNotFound;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DomainExceptionMapper implements ExceptionMapper<DomainException> {
    @Override
    public Response toResponse(DomainException exception) {
        int status = exception instanceof InspectionNotFound ? 404 : 400;
        return Response.status(status).entity(new ErrorBody(exception.getMessage())).build();
    }

    public record ErrorBody(String message) {}
}
