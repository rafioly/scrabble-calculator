package org.zuehlke.gobusiness.scrabble.calculator.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.zuehlke.gobusiness.scrabble.calculator.dto.ApiResponse;

/**
 * Global exception handler to catch exceptions from all controllers
 * and return a standardized, user-friendly API response.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles the exception thrown when a submitted word fails validation rules.
     *
     * @param ex The exception that was thrown, containing the specific validation message.
     * @return A ResponseEntity with a 400 Bad Request status and a clear error message.
     */
    @ExceptionHandler(InvalidWordException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidWord(InvalidWordException ex, WebRequest request) {
        ApiResponse<Object> apiResponse = ApiResponse.error(ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST); // 400 Bad Request
    }

    /**
     * Handles the exception thrown when a database constraint is violated,
     * such as a unique constraint on the 'word' column.
     *
     * @param ex The exception that was thrown.
     * @return A ResponseEntity with a 409 Conflict status and a clear error message.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        String message = "This word already exists on the leaderboard. Please submit a unique word.";
        ApiResponse<Object> apiResponse = ApiResponse.error(message);
        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT); // 409 Conflict
    }
}
