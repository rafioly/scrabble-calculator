package org.zuehlke.gobusiness.scrabble.calculator.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorMessage {

    public static final String EMPTY_WORD = "Word must not be null or empty.";
    public static final String WORD_CONTAINS_NON_ALPHABETIC_CHARS = "Word must only contain alphabetic characters.";
    public static final String WORD_LENGTH_INVALID = "Word length must be between %d and %d characters.";

    public static final String WORD_LENGTH_INVALID_BEAN = "Word length must be between {min} and {max} characters.";
}
