package org.airport.example.rest.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Request model is used for User Login.
 */
@Data
public class UserLogin {
    /**
     * User's email
     */
    @NotNull @Email
    private String email;
    /**
     * User's password
     */
    @NotEmpty
    private String password;
}
