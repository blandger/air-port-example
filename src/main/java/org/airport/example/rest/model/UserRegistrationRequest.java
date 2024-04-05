package org.airport.example.rest.model;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * Request model is used for User Registration.
 */
@Data
public class UserRegistrationRequest {
    /**
     * User's full name
     */
    @NotNull @NotEmpty @Size(max = 250, min = 5) @Pattern(regexp = "[A-Za-z0-9 ]*", message = "must contain only letters, digits and spaces (no special symbols)")
    private String username;
    /**
     * User's email, unique
     */
    @NotNull @NotEmpty @Size(max = 60, min = 5) @Email
    private String email;
    /**
     * Registration password
     */
    @NotEmpty @NotEmpty @Size(max = 45, min = 8)
    private String password;
}
