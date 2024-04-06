package org.airport.example.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.airport.example.exception.UserLoginException;
import org.airport.example.exception.UserRegistrationException;
import org.airport.example.mapper.UserEntityModelMapper;
import org.airport.example.model.UserModel;
import org.airport.example.repository.UserRepository;
import org.airport.example.repository.entity.UserEntity;

import java.util.Objects;

/**
 * Service for business logic related to User management like
 * - registering new user
 * - do user login by checking records, verifying password and returning JWT token
 */
@ApplicationScoped
@Slf4j
public class UserService {
    @Inject
    private UserRepository userRepository;
    @Inject
    private PasswordService passwordService;
    @Inject
    private TokenService tokenService;
    @Inject
    private UserEntityModelMapper userMapper;

    /**
     * Do user registration. Try to create new User or return exception.
     *
     * @param model user's data
     * @return model if user is created, fails otherwise
     * @throws RuntimeException on duplication, sql errors, data saving error, etc
     */
    @Transactional
    public UserModel register(UserModel model) throws UserRegistrationException {
        Objects.requireNonNull(model, "user model is NULL");
        var entity = userMapper.toEntity(model);
//        log.debug("Prepared Registration entity: {}", entity);
        System.out.println("Prepared Registration entity = " + entity);
        try {
            userRepository.createUser(entity);
            log.debug("Registered: {}", entity);
        } catch (Exception e) {
            throw new UserRegistrationException("Failed user registration", e);
        }
        return userMapper.toModel(entity);
    }

    /**
     * User login logic includes steps:
     * - check if user exists in database
     * - check if user's hashed password can be verified against database field value
     * - generate new JWT token
     * @param model user data
     * @return JWT token
     * @throws UserLoginException if something goes wrong
     */
    public String login(UserModel model) throws UserLoginException {
//        log.debug("Do login: {}", model);
        System.out.println("Do login: " + model);
        Objects.requireNonNull(model, "user model is NULL");
        Objects.requireNonNull(model.getEmail(), "user email is NULL");
        Objects.requireNonNull(model.getPassword(), "user password is NULL");

        var foundUser = userRepository.findByEmail(model.getEmail());
        if (foundUser == null) {
            log.warn("User '{}' not found", model.getEmail());
            throw new UserLoginException("User is not known");
        }
        // verify password
        var hashedPassword = passwordService.hashPassword(model.getPassword().toCharArray());
        if (!passwordService.isCorrectPassword(foundUser.getPassword(), hashedPassword)) {
            log.warn("User '{}', password verification has failed", model.getEmail());
            throw new UserLoginException("User's is not authorized, incorrect password");
        }

        String jwtResponse;
        try {
            // generate JWT
            jwtResponse = tokenService.generateJWT(true, model.getEmail(), "user");
            System.out.printf("'%s' JWT = [%d]\n", model.getEmail(), jwtResponse.length());
        } catch (Exception e) {
            log.warn("User '{}', jwt generation has failed", model.getEmail(), e);
            throw new UserLoginException("JWT generation is failed");
        }
        return jwtResponse;
    }

    public UserModel findByEmail(String email) {
        Objects.requireNonNull(email, "user email is NULL");
        UserEntity foundUserEntity = userRepository.findByEmail(email);
        if (foundUserEntity != null) {
            return userMapper.toModel(foundUserEntity);
        }
        return null;
    }

    public String hello(String name, String caller) {
        var result = String.format("Hello '%s' (%s).", name, caller);
        log.debug("{}", result);
        return result;
    }
}