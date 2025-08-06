package com.example.ejercicio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final String RESOURCE_NOT_FOUND_ERROR = "Recurso no encontrado";
    private static final String DUPLICATE_RESOURCE_ERROR = "Recurso duplicado";
    private static final String VALIDATION_ERROR = "Error de validación";
    private static final String VALIDATION_ERROR_MESSAGE = "Los datos enviados no son válidos";
    private static final String INSUFFICIENT_BALANCE_ERROR = "Saldo insuficiente";
    private static final String INVALID_ARGUMENT_ERROR = "Argumento inválido";
    private static final String INTERNAL_SERVER_ERROR = "Error interno del servidor";
    private static final String UNEXPECTED_ERROR_MESSAGE = "Ha ocurrido un error inesperado. Por favor, contacte al administrador.";
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse errorResponse = createErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                RESOURCE_NOT_FOUND_ERROR,
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(DuplicateResourceException ex) {
        ErrorResponse errorResponse = createErrorResponse(
                HttpStatus.CONFLICT.value(),
                DUPLICATE_RESOURCE_ERROR,
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = extractValidationErrors(ex);
        
        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                VALIDATION_ERROR,
                VALIDATION_ERROR_MESSAGE,
                errors,
                LocalDateTime.now()
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<ErrorResponse> handleSaldoInsuficienteException(SaldoInsuficienteException ex) {
        ErrorResponse errorResponse = createErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                INSUFFICIENT_BALANCE_ERROR,
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse errorResponse = createErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                INVALID_ARGUMENT_ERROR,
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                INTERNAL_SERVER_ERROR,
                UNEXPECTED_ERROR_MESSAGE
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    private ErrorResponse createErrorResponse(int status, String error, String message) {
        return new ErrorResponse(status, error, message, LocalDateTime.now());
    }
    
    private Map<String, String> extractValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
    
    public static class ErrorResponse {
        private int status;
        private String error;
        private String message;
        private LocalDateTime timestamp;
        
        public ErrorResponse(int status, String error, String message, LocalDateTime timestamp) {
            this.status = status;
            this.error = error;
            this.message = message;
            this.timestamp = timestamp;
        }
        
        // Getters and setters
        public int getStatus() { return status; }
        public void setStatus(int status) { this.status = status; }
        
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    }
    
    public static class ValidationErrorResponse extends ErrorResponse {
        private Map<String, String> validationErrors;
        
        public ValidationErrorResponse(int status, String error, String message, 
                                     Map<String, String> validationErrors, LocalDateTime timestamp) {
            super(status, error, message, timestamp);
            this.validationErrors = validationErrors;
        }
        
        public Map<String, String> getValidationErrors() { return validationErrors; }
        public void setValidationErrors(Map<String, String> validationErrors) { 
            this.validationErrors = validationErrors; 
        }
    }
}
