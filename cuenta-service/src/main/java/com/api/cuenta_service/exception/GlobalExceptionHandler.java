package com.api.cuenta_service.exception;

import com.api.cuenta_service.dto.ErrorResponse;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<Map<String, String>> handleSaldoInsuficienteException(SaldoInsuficienteException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    @ExceptionHandler(CuentaNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCuentaNotFoundException(CuentaNotFoundException ex) {
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