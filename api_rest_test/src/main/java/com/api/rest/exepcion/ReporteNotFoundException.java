package com.api.rest.exepcion;

public class ReporteNotFoundException extends RuntimeException {

    public ReporteNotFoundException(String message) {
        super(message);
    }

    public ReporteNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
