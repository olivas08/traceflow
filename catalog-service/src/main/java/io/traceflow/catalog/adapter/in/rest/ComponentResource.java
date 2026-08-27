package io.traceflow.catalog.adapter.in.rest;

import io.traceflow.catalog.domain.port.in.GetComponentUseCase;
import io.traceflow.catalog.domain.port.in.GetTraceUseCase;
import io.traceflow.catalog.domain.port.in.ListComponentsUseCase;
import io.traceflow.catalog.domain.port.in.RegisterComponentUseCase;
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

@Path("/api/v1/components")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ComponentResource {
    private final RegisterComponentUseCase registerComponent;
    private final GetComponentUseCase getComponent;
    private final ListComponentsUseCase listComponents;
    private final GetTraceUseCase getTrace;

    public ComponentResource(
            RegisterComponentUseCase registerComponent,
            GetComponentUseCase getComponent,
            ListComponentsUseCase listComponents,
            GetTraceUseCase getTrace) {
        this.registerComponent = registerComponent;
        this.getComponent = getComponent;
        this.listComponents = listComponents;
        this.getTrace = getTrace;
    }

    @POST
    public Response register(
            RegisterComponentRequest request,
            @HeaderParam("X-Correlation-Id") String correlationId) {
        var component =
                registerComponent.execute(
                        new RegisterComponentUseCase.Command(
                                request.plantCode(),
                                request.serialNumber(),
                                request.partNumber(),
                                request.parentSerialNumber(),
                                correlationId(correlationId)));
        ComponentResponse body = CatalogRestMapper.toResponse(component);
        return Response.created(URI.create("/api/v1/components/" + body.serialNumber()))
                .entity(body)
                .build();
    }

    @GET
    public PageResponse<ComponentResponse> list(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {
        return CatalogRestMapper.toPage(
                listComponents.execute(page, size), CatalogRestMapper::toResponse);
    }

    @GET
    @Path("/{serial}")
    public ComponentResponse get(@PathParam("serial") String serial) {
        return CatalogRestMapper.toResponse(getComponent.execute(serial));
    }

    @GET
    @Path("/{serial}/trace")
    public TraceResponse trace(@PathParam("serial") String serial) {
        return CatalogRestMapper.toResponse(getTrace.execute(serial));
    }

    private static String correlationId(String header) {
        if (header == null || header.isBlank()) {
            return UUID.randomUUID().toString();
        }
        return header;
    }
}
