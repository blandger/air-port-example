package org.airport.example.rest;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.airport.example.exception.AirPortCreateException;
import org.airport.example.mapper.AirPortEntityModelMapper;
import org.airport.example.model.AirPortModel;
import org.airport.example.model.UserModel;
import org.airport.example.rest.request.AirPortCreateRequest;
import org.airport.example.service.AirPortService;
import org.airport.example.service.UserService;

import java.security.Principal;
import java.util.List;

/**
 * Controller/EndPoint is used for User management and JWT token operations.
 * - create new AirPort only by authorized user (with jwt token)
 * - update AirPort data by authorized user
 * - delete AirPort data by authorized user
 * - get all airports list (by anyone)
 * - find by name (by anyone)
 * - find by code (by anyone)
 */
@Path("/airports")
@Slf4j
public class AirPortEndpoint {
    @Inject
    private AirPortService portService;
    @Inject
    private AirPortEntityModelMapper airPortMapper;
    @Inject
    private UserService userService;

    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user"})
    public Response create(
            @Context SecurityContext securityContext,
            @NotNull @Valid AirPortCreateRequest createRequest) {
//        log.debug("Create AirPort = {}", createRequest);
        System.out.println("Create AirPort: " + createRequest);
        Principal principal = securityContext.getUserPrincipal();
        String jwtTokenPrincipal = principal == null ? "anonymous" : principal.getName();
        System.out.println("Create AirPort jwtTokenPrincipal = " + jwtTokenPrincipal);

        AirPortModel airPortModel = airPortMapper.requestToModel(createRequest);
        UserModel foundUser = userService.findByEmail(jwtTokenPrincipal);
        if (foundUser == null) {
            log.warn("User '{}' not found", jwtTokenPrincipal);
            throw new AirPortCreateException("Participated user is not known");
        }

        AirPortModel newModel;
        try {
            newModel = portService.create(airPortModel, foundUser);
        } catch (RuntimeException e) {
            log.error("AirPort creation error", e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("AirPort creation error: " + e.getMessage()).build();
        }
        return Response.status(Response.Status.CREATED.getStatusCode()).entity(newModel).build();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getAll(@Context SecurityContext securityContext) {
        Principal principal = securityContext.getUserPrincipal();
        String caller = principal == null ? "anonymous" : principal.getName();
        System.out.println("caller = " + caller);

        List<AirPortModel> responseList = portService.getAll();
//        log.debug("Found AirPorts : [{}]", responseList.size());
        System.out.println("Found AirPorts : " + responseList.size());
        return Response.status(Response.Status.OK.getStatusCode()).entity(responseList).build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getByName(
            final @QueryParam("name") String name,
            final @QueryParam("code") String code,
            final @QueryParam("city") String city) {
        System.out.println("#S#% Get AirPorts by name : " + name + ", " + code + ", " + city);
        List<AirPortModel> responseList = portService.getByName(name);
        log.debug("Found by name '{}': [{}]", name, responseList.size());
        return Response.status(Response.Status.OK.getStatusCode()).entity(responseList).build();
    }

}
