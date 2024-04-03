package org.airport.example.service;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class UserService {

    public String hello(String name, String caller) {
        var result = String.format("Hello '%s' (%s).", name, caller);
        log.debug("{}", result);
        return result;
    }
}