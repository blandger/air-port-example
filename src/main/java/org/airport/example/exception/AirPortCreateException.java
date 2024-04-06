package org.airport.example.exception;

/**
 * AirPort creation failed exception
 */
    public class AirPortCreateException extends RuntimeException {

    public AirPortCreateException(String message) {
        super(message);
    }

    public AirPortCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public AirPortCreateException(Throwable cause) {
        super(cause);
    }

}
