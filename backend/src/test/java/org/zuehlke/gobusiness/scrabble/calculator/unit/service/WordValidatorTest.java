package org.zuehlke.gobusiness.scrabble.calculator.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.zuehlke.gobusiness.scrabble.calculator.exception.InvalidWordException;
import org.zuehlke.gobusiness.scrabble.calculator.validator.WordValidator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Unit Tests for Word Validator")
class WordValidatorTest {

    private WordValidator wordValidator;

    @BeforeEach
    void setUp() {
        wordValidator = new WordValidator();
    }

    @Test
    @DisplayName("Should pass for a valid word")
    void validate_withValidWord_doesNotThrow() {
        assertDoesNotThrow(() -> wordValidator.validate("hello"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    @DisplayName("Should throw InvalidWordException for null or blank words")
    void validate_withNullOrBlank_throwsException(String word) {
        assertThrows(InvalidWordException.class, () -> wordValidator.validate(word));
    }

    @Test
    @DisplayName("Should throw InvalidWordException for words with non-alphabetic characters")
    void validate_withNonAlphabetic_throwsException() {
        assertThrows(InvalidWordException.class, () -> wordValidator.validate("hello123"));
        assertThrows(InvalidWordException.class, () -> wordValidator.validate("hello!"));
        assertThrows(InvalidWordException.class, () -> wordValidator.validate("hel lo"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "abcdefghijklmnop"})
    @DisplayName("Should throw InvalidWordException for words with invalid length")
    void validate_withInvalidLength_throwsException(String word) {
        assertThrows(InvalidWordException.class, () -> wordValidator.validate(word));
    }
}
