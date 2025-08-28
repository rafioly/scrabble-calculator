package org.zuehlke.gobusiness.scrabble.calculator.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public record LeaderboardEntryDto(Optional<Integer> rank, String word, int score) {
}
