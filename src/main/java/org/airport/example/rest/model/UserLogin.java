package org.airport.example.rest.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Request model is used for User Login.
 */
@Data
public class UserLogin {
    /**
     * User's email
     */
    @Size(max = 60, min = 5) @Email
    private String email;
    /**
     * User's password
     */
    @NotEmpty @Size(max = 45, min = 8)
    private String password;
}
