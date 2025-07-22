package com.example.userService.exceptions;

import com.example.userService.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler for REST controllers.
 * <p>
 * Captures and handles exceptions thrown from anywhere in the application
 * and converts them into consistent {@link ErrorResponse} objects.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link UserAlreadyExistException} and returns a 400 Bad Request.
     *
     * @param ex the exception instance
     * @return a structured error response with timestamp and message
     */
    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistException(UserAlreadyExistException ex) {
        var errorDetail = new ErrorResponse.ErrorDetail(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(new ErrorResponse(List.of(errorDetail)), HttpStatus.BAD_REQUEST);
    }
    /**
     * Handles {@link UserNotFoundException} and returns a 404 Not Found.
     *
     * @param ex the exception instance
     * @return a 404 response without body
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        return  ResponseEntity.notFound().build();
    }

    /**
     * Handles validation errors triggered by {@code @Valid} annotated DTOs.
     *
     * @param ex the validation exception
     * @return a list of field-specific validation errors with 400 Bad Request status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> new ErrorResponse.ErrorDetail(
                        Instant.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        err.getField() + ": " + err.getDefaultMessage()
                ))
                .collect(Collectors.toList());
        return new ResponseEntity<>(new ErrorResponse(errors), HttpStatus.BAD_REQUEST);
    }

    /**
     * Catches all other unhandled exceptions and returns a generic error message.
     *
     * @param ex the unexpected exception
     * @return a 500 Internal Server Error with a generic error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        var errorDetail = new ErrorResponse.ErrorDetail(
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal error: something went wrong"
        );
        return new ResponseEntity<>(new ErrorResponse(List.of(errorDetail)), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}