package org.zuehlke.gobusiness.scrabble.calculator.exception;

/**
 * Custom exception thrown when a submitted word fails validation.
 */
public class InvalidWordException extends RuntimeException {
    public InvalidWordException(String message) {
        super(message);
    }
}
