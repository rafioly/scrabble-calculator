package org.zuehlke.gobusiness.scrabble.calculator.service;

import org.springframework.stereotype.Component;
import org.zuehlke.gobusiness.scrabble.calculator.exception.InvalidWordException;

@Component
public class WordValidator {

    private static final int MIN_WORD_LENGTH = 2;
    private static final int MAX_WORD_LENGTH = 15;

    /**
     * Validates a word based on a set of rules.
     *
     * @param word The word to validate.
     * @throws InvalidWordException if the word fails any validation rule.
     */
    public void validate(String word) throws InvalidWordException {
        if (word == null || word.isBlank()) {
            throw new InvalidWordException("Word must not be null or empty.");
        }

        if (!word.chars().allMatch(Character::isLetter)) {
            throw new InvalidWordException("Word must only contain alphabetic characters.");
        }

        if (word.length() < MIN_WORD_LENGTH || word.length() > MAX_WORD_LENGTH) {
            throw new InvalidWordException(String.format("Word length must be between %d and %d characters.", MIN_WORD_LENGTH, MAX_WORD_LENGTH));
        }
    }
}
