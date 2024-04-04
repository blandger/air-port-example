package org.airport.example.rest.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Request model is used for User Registration.
 */
@Data
public class UserRegistrationRequest {
    /**
     * User's full name
     */
    @NotNull @Size(max = 25, min = 5)
    private String fullName;
    /**
     * User's email, unique
     */
    @Size(max = 60, min = 5) @Email
    private String email;
    /**
     * Registration password
     */
    @NotEmpty @Size(max =45, min = 8)
    private String password;
}
