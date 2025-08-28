package org.zuehlke.gobusiness.scrabble.calculator.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.zuehlke.gobusiness.scrabble.calculator.dto.ApiResponse;

import java.util.stream.Collectors;

/**
 * Global exception handler to catch exceptions from all controllers
 * and return a standardized, user-friendly API response.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodValidationFailed(MethodArgumentNotValidException ex, WebRequest request) {
        ApiResponse<Object> apiResponse = ApiResponse.error(
                ex.getBindingResult().getFieldErrors()
                        .stream().map(FieldError::getDefaultMessage)
                        .collect(Collectors.joining(", ")));
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidWordException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidWord(InvalidWordException ex, WebRequest request) {
        ApiResponse<Object> apiResponse = ApiResponse.error(ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        String message = "This word already exists on the leaderboard. Please submit a unique word.";
        ApiResponse<Object> apiResponse = ApiResponse.error(message);
        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
    }
}
