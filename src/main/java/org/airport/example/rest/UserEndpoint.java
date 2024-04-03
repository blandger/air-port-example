package org.airport.example.rest;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.airport.example.service.UserService;

import java.security.Principal;

@Slf4j
@Path("/users")
public class UserEndpoint {

    @Inject
    private UserService service;

    @GET
    @Path("/me/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response whoIam(
            @Context SecurityContext securityContext,
            final @PathParam("name") String name) {
        Principal principal = securityContext.getUserPrincipal();
        String caller = principal == null ? "anonymous" : principal.getName();

        String response = service.hello(name, caller);
        log.debug("{}", response);
        return Response.ok(response).build();
    }

    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response register() {
        return Response.ok("OK").build();
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response login() {
        return Response.ok("OK").build();
    }
}
