package org.airport.example.model;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * Internal user model.
 */
@Data
public class UserModel {
    private Long id;
    private String username;
    private String email;
    @ToString.Exclude
    private String password;
    private Date created;
}
