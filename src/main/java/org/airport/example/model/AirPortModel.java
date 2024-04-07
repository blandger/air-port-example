package org.airport.example.model;

import lombok.Data;

import java.util.Date;

/**
 *  AirPort model. it's used internally and used as rest api response
 */
@Data
public class AirPortModel {
    private Long id;
    private String name;
    private String code;
    private String city;
    private String created_by; // not ready yet
    private Date created;
}
