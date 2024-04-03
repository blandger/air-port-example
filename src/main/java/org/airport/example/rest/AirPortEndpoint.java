package org.airport.example.rest;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.airport.example.rest.model.AirPortCreateRequest;
import org.airport.example.service.AirPortService;

import javax.validation.Valid;
import java.util.List;

/**
 * Controller/EndPoint is used for User management and JWT token operations.
 */
@Path("/airports")
@Slf4j
public class AirPortEndpoint {

    @Inject
    private AirPortService service;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getAll() {
        List<String> response = service.getAll();
        log.debug("{}", response);
        return Response.ok(response).build();
    }

    @GET
    @Path("/name/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getByName(
            final @PathParam("name") String name) {

        String response = service.getByName(name);
        log.debug("{}", response);
        return Response.ok(response).build();
    }

    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(
            @Context SecurityContext securityContext,
            @Valid AirPortCreateRequest createRequest) {
        log.debug("Create AirPort = {}", createRequest);
        String response = "DONE new AirPort";
        return Response.ok(response).build();
    }
}
