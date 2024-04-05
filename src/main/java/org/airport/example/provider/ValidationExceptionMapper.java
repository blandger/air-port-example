package org.airport.example.provider;

import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

/**
 * Component for error conversion and mapping
 */
@Slf4j
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {
    @Override
    public Response toResponse(ValidationException exception) {
        //
        String error_message = exception.toString();
        log.error("Validation Error {}", error_message);
        return Response.ok(error_message).build();
    }
}