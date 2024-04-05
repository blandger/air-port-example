package org.airport.example.rest.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Request model is used for creating new AirPort.
 */
@Data
public class AirPortCreateRequest {
    /**
     * Airport name, not unique
     */
    @NotNull @NotEmpty @Size(max = 250, min = 5)
    private String name;
    /**
     * Airport code, unique
     */
    @NotNull @NotEmpty @Size(max = 25, min = 3)
    private String code;
    /**
     * Airport city name, not unique
     */
    @NotNull @NotEmpty @Size(max = 250, min = 5)
    private String city;
}
