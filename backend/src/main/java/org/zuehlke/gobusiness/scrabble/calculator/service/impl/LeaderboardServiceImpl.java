package org.zuehlke.gobusiness.scrabble.calculator.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.zuehlke.gobusiness.scrabble.calculator.domain.LeaderboardEntry;
import org.zuehlke.gobusiness.scrabble.calculator.dto.LeaderboardEntryDto;
import org.zuehlke.gobusiness.scrabble.calculator.repository.LeaderboardRepository;
import org.zuehlke.gobusiness.scrabble.calculator.service.LeaderboardService;
import org.zuehlke.gobusiness.scrabble.calculator.service.ScoringService;
import org.zuehlke.gobusiness.scrabble.calculator.validator.WordValidator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Profile("!caching") // Active by default, when 'caching' profile is NOT active
@RequiredArgsConstructor
public class LeaderboardServiceImpl implements LeaderboardService {

    private final LeaderboardRepository leaderboardRepository;
    private final ScoringService scoringService;
    private final WordValidator wordValidator;

    @Override
    public LeaderboardEntryDto saveNewScore(String word) {
        // First, normalize the word to ensure consistency.
        final String normalizedWord = word.trim().toUpperCase();

        // Then, validate the normalized word. This will throw an exception if the word is invalid.
        wordValidator.validate(normalizedWord);

        // If validation passes, proceed with scoring and saving using the normalized word.
        int score = scoringService.calculateScore(normalizedWord);
        LeaderboardEntry entryToSave = new LeaderboardEntry(normalizedWord, score);
        leaderboardRepository.save(entryToSave);
        
        return new LeaderboardEntryDto(Optional.empty(), normalizedWord, score);
    }

    @Override
    public List<LeaderboardEntryDto> getLeaderboard(int top) {
        List<LeaderboardEntry> entries = leaderboardRepository.findByOrderByScoreDesc(PageRequest.of(0, top));
        return IntStream.range(0, entries.size())
                .mapToObj(i -> {
                    LeaderboardEntry dbEntry = entries.get(i);
                    return new LeaderboardEntryDto(Optional.of(i + 1), dbEntry.getWord(), dbEntry.getScore());
                })
                .collect(Collectors.toList());
    }
}
