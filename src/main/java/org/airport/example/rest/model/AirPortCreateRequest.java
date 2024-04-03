package org.airport.example.rest.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Request model is used for creating new AirPort.
 */
@Data
public class AirPortCreateRequest {
    /**
     * Airport name, not unique
     */
    @NotNull @Size(max=250)
    private String name;
    /**
     * Airport code, unique
     */
    @NotNull @Size(max=25)
    private String code;
    /**
     * Airport city name, not unique
     */
    @NotNull @Size(max=250)
    private String city;
}
