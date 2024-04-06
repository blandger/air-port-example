package org.airport.example.rest.respose;

import lombok.Data;

/**
 * REST API Response after User registration
 */
@Data
public class UserRegistrationResponse {
    private Long id;
    private String username;
    private String email;
}
