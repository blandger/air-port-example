package org.airport.example.rest.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Request model is used for User Login.
 */
@Data
public class UserLoginRequest {
    /**
     * User's email
     */
    @NotNull @NotEmpty @Size(max = 60, min = 5) @Email
    private String email;
    /**
     * User's password
     */
    @NotEmpty @NotEmpty @Size(max = 45, min = 8)
    private String password;
}
