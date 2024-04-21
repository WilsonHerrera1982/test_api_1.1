package com.api.rest.exepcion;

public class MovimientoNotFoundException extends RuntimeException {

    public MovimientoNotFoundException(String message) {
        super(message);
    }

    public MovimientoNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
