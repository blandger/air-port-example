package org.airport.example.exception;

/**
 * User failed login exception
 */
public class UserLoginException extends RuntimeException {

    public UserLoginException(String message) {
        super(message);
    }

    public UserLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserLoginException(Throwable cause) {
        super(cause);
    }

}
