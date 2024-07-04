package com.api.cliente_service.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.api.cliente_service.dto.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClienteNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClienteNotFoundException(ClienteNotFoundException ex) {
        int status = HttpStatus.NOT_FOUND.value();
        String message = ex.getMessage();
        long timestamp = System.currentTimeMillis();
        ErrorResponse error = new ErrorResponse(status, message, timestamp);
        error.setMessage(message);
        error.setStatus(status);
        error.setTimestamp(timestamp);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }  
}