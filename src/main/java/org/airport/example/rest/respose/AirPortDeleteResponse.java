package org.airport.example.rest.respose;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * REST API response returned on AirPort deletion
 */
@Data
@AllArgsConstructor
public class AirPortDeleteResponse {
    /**
     * AirPort PK ID
     */
    private Long id;
    /**
     * True if deleted successfully, false otherwise
     */
    private boolean deleted;
    /**
     * Possible error message if happens
     */
    private String error;
}
