package org.airport.example;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserService {

    public String hello(String name) {
        return String.format("Hello '%s'.", name);
    }
}