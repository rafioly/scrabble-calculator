package org.zuehlke.gobusiness.scrabble.calculator.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.zuehlke.gobusiness.scrabble.calculator.domain.LeaderboardEntry;
import org.zuehlke.gobusiness.scrabble.calculator.dto.LeaderboardEntryDto;
import org.zuehlke.gobusiness.scrabble.calculator.repository.LeaderboardRepository;
import org.zuehlke.gobusiness.scrabble.calculator.service.LeaderboardService;
import org.zuehlke.gobusiness.scrabble.calculator.service.ScoringService;
import org.zuehlke.gobusiness.scrabble.calculator.service.WordValidator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Profile("caching") // Only active when the 'caching' profile is enabled
@RequiredArgsConstructor
public class CachingLeaderboardServiceImpl implements LeaderboardService {

    private final LeaderboardRepository leaderboardRepository;
    private final ScoringService scoringService;
    private final WordValidator wordValidator; // Injected the validator

    @Override
    @CacheEvict(value = "leaderboard", allEntries = true) // Clear the cache on save
    public LeaderboardEntryDto saveNewScore(String word) {
        // First, validate the word. This will throw an exception if the word is invalid.
        wordValidator.validate(word);

        // If validation passes, proceed with scoring and saving.
        int score = scoringService.calculateScore(word);
        LeaderboardEntry entry = new LeaderboardEntry(word, score);
        leaderboardRepository.save(entry);
        return new LeaderboardEntryDto(Optional.empty(), word, score);
    }

    @Override
    public List<LeaderboardEntryDto> getLeaderboard(int top) {
        // Get the full leaderboard (from cache or DB)
        List<LeaderboardEntry> fullLeaderboard = getFullLeaderboard();

        // Take the top N entries and map them to DTOs with ranks
        return IntStream.range(0, Math.min(top, fullLeaderboard.size()))
                .mapToObj(i -> {
                    LeaderboardEntry entry = fullLeaderboard.get(i);
                    return new LeaderboardEntryDto(Optional.of(i + 1), entry.getWord(), entry.getScore());
                })
                .collect(Collectors.toList());
    }

    @Cacheable("leaderboard") // Cache the result of this method
    public List<LeaderboardEntry> getFullLeaderboard() {
        // This method will only be executed if the cache is empty.
        // Otherwise, the result is returned directly from the cache.
        return leaderboardRepository.findAllByOrderByScoreDesc();
    }
}
