package org.zuehlke.gobusiness.scrabble.calculator.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Optional;

/**
 * Represents a single entry on the leaderboard.
 * The 'rank' is optional because it is only present when fetching the full leaderboard list.
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public record LeaderboardEntryDto(Optional<Integer> rank, String word, int score) {
}
