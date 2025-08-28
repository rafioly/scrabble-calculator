package org.zuehlke.gobusiness.scrabble.calculator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.zuehlke.gobusiness.scrabble.calculator.constant.ErrorMessage;

public record WordSubmissionDto(
        @NotBlank(message = ErrorMessage.EMPTY_WORD)
        @Size(min = 2, max = 15, message = ErrorMessage.WORD_LENGTH_INVALID_BEAN)
        @Pattern(regexp = "[a-zA-Z]+", message = ErrorMessage.WORD_CONTAINS_NON_ALPHABETIC_CHARS)
        String word) {
}