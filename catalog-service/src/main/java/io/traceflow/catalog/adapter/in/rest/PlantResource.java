package io.traceflow.catalog.adapter.in.rest;

import io.traceflow.catalog.domain.port.in.ListPlantsUseCase;
import io.traceflow.catalog.domain.port.in.RegisterPlantUseCase;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;

@Path("/api/v1/plants")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PlantResource {
    private final RegisterPlantUseCase registerPlant;
    private final ListPlantsUseCase listPlants;

    public PlantResource(RegisterPlantUseCase registerPlant, ListPlantsUseCase listPlants) {
        this.registerPlant = registerPlant;
        this.listPlants = listPlants;
    }

    @POST
    public Response register(RegisterPlantRequest request) {
        var plant =
                registerPlant.execute(
                        new RegisterPlantUseCase.Command(
                                request.code(), request.name(), request.country()));
        PlantResponse body = CatalogRestMapper.toResponse(plant);
        return Response.created(URI.create("/api/v1/plants/" + body.code())).entity(body).build();
    }

    @GET
    public PageResponse<PlantResponse> list(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {
        return CatalogRestMapper.toPage(
                listPlants.execute(page, size), CatalogRestMapper::toResponse);
    }
}
