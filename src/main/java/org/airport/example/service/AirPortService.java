package org.airport.example.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.airport.example.repository.AirPortRepository;

import java.util.List;

/**
 * Business logic service for AirPort management
 */
@ApplicationScoped
@Slf4j
public class AirPortService {

    @Inject
    AirPortRepository repository;

    /**
     * Get all instances from DB.
     *  TODO! We should do PAGINATION here.
     * @return all instances
     */
    public List<String> getAll() {
        return List.of("Dallas Fort Worth International", "Heathrow Airport");
    }

    public String getByName(String name) {
        return name;
    }

}
