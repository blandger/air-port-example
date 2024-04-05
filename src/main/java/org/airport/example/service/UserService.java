package org.airport.example.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.airport.example.repository.UserRepository;

/**
 * Service for business logic related to User management like
 * - registering new user
 * - do user login and return JWT token
 */
@ApplicationScoped
@Slf4j
public class UserService {

    @Inject
    private UserRepository repository;

    @Inject
    private PasswordService passwordService;

    public String hello(String name, String caller) {
        var result = String.format("Hello '%s' (%s).", name, caller);
        log.debug("{}", result);
        return result;
    }
}