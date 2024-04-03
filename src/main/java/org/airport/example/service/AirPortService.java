package org.airport.example.service;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@ApplicationScoped
@Slf4j
public class AirPortService {

    public List<String> getAll() {
        return List.of("Dallas Fort Worth International", "Heathrow Airport");
    }

    public String getByName(String name) {
        return name;
    }

}
