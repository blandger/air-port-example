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
import org.airport.example.exception.UserLoginException;
import org.airport.example.mapper.UserEntityModelMapper;
import org.airport.example.model.UserModel;
import org.airport.example.rest.request.UserLoginRequest;
import org.airport.example.rest.request.UserRegistrationRequest;
import org.airport.example.service.UserService;

import java.security.Principal;

/**
 * EndPoint controller is used for User management and JWT token operations.
 * Include operations: Register, Login
 */
@Slf4j
@Path("/users")
public class UserEndpoint {
    @Inject
    private UserService userService;
    @Inject
    private UserEntityModelMapper userMapper;

    /**
     * User registration logic, checks request data, stores user to db.
     * @param registrationRequest user's data
     * @return created user model or error
     */
    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response register(@NotNull @Valid UserRegistrationRequest registrationRequest) {
        log.debug("Registration : {}", registrationRequest);
//        System.out.println("Registration : " + registrationRequest);
        UserModel model;
        try {
            model = userService.register(userMapper.requestToModel(registrationRequest));
        } catch (RuntimeException e) {
//            log.error("User registration error", e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("User registration error: " + e.getMessage()).build();
        }
        var response = userMapper.modelToResponse(model);
        return Response.status(Response.Status.CREATED.getStatusCode()).entity(response).build();
    }

    /**
     * Makes user login by checking email + password.
     * @param userLoginRequest user's data
     * @return JWT or error
     */
    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response login(@NotNull @Valid UserLoginRequest userLoginRequest) {
        log.debug("Login : {}", userLoginRequest);
//        System.out.println("Login : " + userLoginRequest.getEmail());
        var userModel = userMapper.loginRequestToModel(userLoginRequest);
        String jtwToken;
        try {
            jtwToken = userService.login(userModel);
        } catch (UserLoginException e) {
//            log.error("User login error", e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Login error: " + e.getMessage()).build();
        }
        return Response.status(Response.Status.OK.getStatusCode()).entity(jtwToken).build();
    }

    /**
     * TODO: NOT IMPLEMENTED due to lack of clear requirements
     *
     * @param securityContext is used for getting security principal
     * @return email
     */
    @POST
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user"})
    public Response logout(@Context SecurityContext securityContext) {
        Principal principal = securityContext.getUserPrincipal();
        String principalEmail = principal == null ? "anonymous" : principal.getName();
        // method is NOT IMPLEMENTED due to lack of clear requirements

        // One of custom scenarios can be following:
        // - take current date-time and update user's record on logout
        // - store that date-time into additional db field like 'logout_date_time' (users table)
        // - when user goes to any other method with required token
        // - compare 'token exp' value with 'logout_date_time' value
        // - if 'token exp' < 'logout_date_time' - FORBID access to method

        return Response.ok(principalEmail).build();
    }
}
