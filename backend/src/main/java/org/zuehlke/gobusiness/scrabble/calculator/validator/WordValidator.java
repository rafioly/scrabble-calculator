package org.zuehlke.gobusiness.scrabble.calculator.validator;

import org.springframework.stereotype.Component;
import org.zuehlke.gobusiness.scrabble.calculator.constant.ErrorMessage;
import org.zuehlke.gobusiness.scrabble.calculator.exception.InvalidWordException;

@Component
public class WordValidator {

    private static final int MIN_WORD_LENGTH = 2;
    private static final int MAX_WORD_LENGTH = 15;

    public void validate(String word) throws InvalidWordException {
        if (word == null || word.isBlank()) {
            throw new InvalidWordException(ErrorMessage.EMPTY_WORD);
        }

        if (!word.chars().allMatch(Character::isLetter)) {
            throw new InvalidWordException(ErrorMessage.WORD_CONTAINS_NON_ALPHABETIC_CHARS);
        }

        if (word.length() < MIN_WORD_LENGTH || word.length() > MAX_WORD_LENGTH) {
            throw new InvalidWordException(String.format(ErrorMessage.WORD_LENGTH_INVALID, MIN_WORD_LENGTH, MAX_WORD_LENGTH));
        }
    }
}
