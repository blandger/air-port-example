package org.airport.example.model;

import lombok.Data;

import java.util.Date;

/**
 *  AirPort model.
 */
@Data
public class AirPortModel {
    private Long id;
    private String name;
    private String code;
    private String city;
//    private String username;
    private Date created;
}
