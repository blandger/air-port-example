package org.airport.example.rest.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

/**
 * Request model is used for User Registration.
 */
@Data
public class UserRegistrationRequest {
    /**
     * User's full name
     */
    @NotNull @Size(max=25)
    private String fullName;
    /**
     * User's email, unique
     */
    @NotNull @Email
    private String email;
    /**
     * Registration password
     */
    @NotEmpty
    private String password;
}
