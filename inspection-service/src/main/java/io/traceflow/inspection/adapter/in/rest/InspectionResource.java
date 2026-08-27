package io.traceflow.inspection.adapter.in.rest;

import io.traceflow.inspection.domain.model.Inspection;
import io.traceflow.inspection.domain.port.in.GetInspectionUseCase;
import io.traceflow.inspection.domain.port.in.ListInspectionsUseCase;
import io.traceflow.inspection.domain.port.in.RecordInspectionUseCase;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.UUID;

@Path("/api/v1/inspections")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InspectionResource {
    private final RecordInspectionUseCase recordInspection;
    private final GetInspectionUseCase getInspection;
    private final ListInspectionsUseCase listInspections;

    public InspectionResource(
            RecordInspectionUseCase recordInspection,
            GetInspectionUseCase getInspection,
            ListInspectionsUseCase listInspections) {
        this.recordInspection = recordInspection;
        this.getInspection = getInspection;
        this.listInspections = listInspections;
    }

    @POST
    public Response record(
            RecordInspectionRequest request,
            @HeaderParam("Idempotency-Key") String idempotencyKey,
            @HeaderParam("X-Correlation-Id") String correlationId) {
        var inspection =
                recordInspection.execute(
                        new RecordInspectionUseCase.Command(
                                request.serialNumber(),
                                request.result(),
                                request.inspector(),
                                request.notes(),
                                idempotencyKey,
                                correlationId(correlationId)));
        InspectionResponse body = toResponse(inspection);
        return Response.created(URI.create("/api/v1/inspections/" + body.id()))
                .entity(body)
                .build();
    }

    @GET
    public PageResponse<InspectionResponse> list(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {
        var result = listInspections.execute(page, size);
        return new PageResponse<>(
                result.items().stream().map(this::toResponse).toList(),
                result.total(),
                result.page(),
                result.size());
    }

    @GET
    @Path("/{id}")
    public InspectionResponse get(@PathParam("id") UUID id) {
        return toResponse(getInspection.execute(id));
    }

    private InspectionResponse toResponse(Inspection inspection) {
        return new InspectionResponse(
                inspection.id(),
                inspection.serialNumber().value(),
                inspection.result().name(),
                inspection.inspector(),
                inspection.notes(),
                inspection.inspectedAt());
    }

    private static String correlationId(String header) {
        if (header == null || header.isBlank()) {
            return UUID.randomUUID().toString();
        }
        return header;
    }
}
