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
import org.airport.example.rest.respose.AirPortDeleteResponse;
import org.airport.example.service.AirPortService;
import org.airport.example.service.UserService;

import java.security.Principal;
import java.util.List;

/**
 * EndPoint controller is used for AirPort management.
 * - create new AirPort only by authorized user (with jwt token)
 * - update AirPort data by authorized user
 * - delete AirPort data by authorized user
 * - get all airports list (by anyone)
 * - find by name (by anyone), code (by anyone) or both params
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

    /**
     * Create new AirPort instance by authorized user.
     *
     * @param securityContext JTW data will be used for identifying User
     * @param createRequest airport data
     * @return created instance or error
     */
    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user"})
    public Response create(
            @Context SecurityContext securityContext,
            @NotNull @Valid AirPortCreateRequest createRequest) {
        log.debug("Create AirPort = {}", createRequest);
//        System.out.println("Create AirPort: " + createRequest);
        Principal principal = securityContext.getUserPrincipal();
        String jwtTokenPrincipal = principal == null ? "anonymous" : principal.getName();
        log.debug("Create AirPort jwtTokenPrincipal: {}", jwtTokenPrincipal);
//        System.out.println("Create AirPort jwtTokenPrincipal = " + jwtTokenPrincipal);

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
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("AirPort creation error: " + e.getMessage()).build();
        }
        log.debug("Created = {}", newModel);
        return Response.status(Response.Status.CREATED.getStatusCode()).entity(newModel).build();
    }

    /**
     * Update AirPort data with new value using known record ID
     *
     * @param securityContext JTW data will be used for identifying User
     * @param id airport Id value
     * @param createRequest new data for update
     * @return updated instance or error
     */
    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user"})
    public Response update(
            @Context SecurityContext securityContext,
            @NotNull @Valid @PathParam("id") Long id,
            @NotNull @Valid AirPortCreateRequest createRequest) {
        log.debug("Update by '{}' AirPort = {}", id, createRequest);
//        System.out.println("Update AirPort: " + createRequest);
        Principal principal = securityContext.getUserPrincipal();
        String jwtTokenPrincipal = principal == null ? "anonymous" : principal.getName();
        log.trace("Update AirPort jwtTokenPrincipal: {}", jwtTokenPrincipal);
//        System.out.println("Update AirPort jwtTokenPrincipal = " + jwtTokenPrincipal);

        AirPortModel airPortModel = airPortMapper.requestToModel(createRequest);
        airPortModel.setId(id);
        UserModel foundUser = userService.findByEmail(jwtTokenPrincipal);
        if (foundUser == null) {
            log.warn("User '{}' not found", jwtTokenPrincipal);
            throw new AirPortCreateException("Participated user is not known");
        }

        AirPortModel newModel;
        try {
            newModel = portService.update(airPortModel, foundUser);
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("AirPort updating error: " + e.getMessage()).build();
        }
        log.debug("Updated = {}", newModel);
        return Response.status(Response.Status.ACCEPTED.getStatusCode()).entity(newModel).build();
    }

    /**
     * Delete record by authorized user and known airport Id
     *
     * @param id airport ID
     * @return true if deleted successfully, false if record not found, error if something failed
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user"})
    public Response delete(
            @NotNull @Valid @PathParam("id") Long id) {
        log.debug("Deleting airport by '{}'", id);
//        System.out.println("Deleting AirPort: " + id);
        AirPortDeleteResponse deleteResponse = new AirPortDeleteResponse(id, false, null);
        try {
            int deleted = portService.delete(id);
            deleteResponse.setDeleted(deleted > 0);
        } catch (Exception e) {
            deleteResponse.setError("AirPort deleting error: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(deleteResponse).build();
        }
        log.debug("Deleted airport: '{}'", deleteResponse);
        return Response.status(Response.Status.ACCEPTED.getStatusCode()).entity(deleteResponse).build();
    }

    /**
     * Search AirPort by using different parameter.
     * Zero values, first, second or both parameter values can be used.
     * TODO: PAGINATION should be used here, but that was not required
     *
     * @param name is optional
     * @param code is optional
     * @return found instances
     */
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getByParameters(
            @QueryParam("name") String name,
            @QueryParam("code") String code) {
        log.debug("Get AirPorts by name: '{}', code: '{}'", name, code);
//        System.out.println("Get AirPorts by name : " + name + ", code = " + code);
        List<AirPortModel> responseList = portService.getByParameters(name, code);
//        List<AirPortModel> responseList = portService.getByName(name, code);
        log.debug("Found airport(s) by name '{}', code '{}' : [{}]", name, code, responseList.size());
        return Response.status(Response.Status.OK.getStatusCode()).entity(responseList).build();
    }

}
