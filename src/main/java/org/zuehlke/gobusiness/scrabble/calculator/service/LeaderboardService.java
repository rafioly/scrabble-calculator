package org.zuehlke.gobusiness.scrabble.calculator.service;

import org.zuehlke.gobusiness.scrabble.calculator.dto.LeaderboardEntryDto;

import java.util.List;

public interface LeaderboardService {
    LeaderboardEntryDto saveNewScore(String word);
    List<LeaderboardEntryDto> getLeaderboard(int top);
}
