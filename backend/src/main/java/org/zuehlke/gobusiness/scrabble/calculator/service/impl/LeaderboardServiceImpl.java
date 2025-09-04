package org.zuehlke.gobusiness.scrabble.calculator.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.zuehlke.gobusiness.scrabble.calculator.domain.LeaderboardEntry;
import org.zuehlke.gobusiness.scrabble.calculator.dto.LeaderboardEntryDto;
import org.zuehlke.gobusiness.scrabble.calculator.exception.InvalidWordException;
import org.zuehlke.gobusiness.scrabble.calculator.repository.LeaderboardRepository;
import org.zuehlke.gobusiness.scrabble.calculator.service.DictionaryService;
import org.zuehlke.gobusiness.scrabble.calculator.service.LeaderboardService;
import org.zuehlke.gobusiness.scrabble.calculator.service.ScoringService;
import org.zuehlke.gobusiness.scrabble.calculator.validator.WordValidator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class LeaderboardServiceImpl implements LeaderboardService {

    private final LeaderboardRepository leaderboardRepository;
    private final ScoringService scoringService;
    private final WordValidator wordValidator;
    private final DictionaryService dictionaryService;

    @Override
    public LeaderboardEntryDto saveNewScore(String word) {

        final String normalizedWord = word.trim().toUpperCase();

        wordValidator.validate(normalizedWord);

        if (!dictionaryService.isValidWord(normalizedWord)) {
            throw new InvalidWordException("'" + normalizedWord + "' is not a valid English word.");
        }

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

    @Override
    public void deleteAllScores() {
        leaderboardRepository.deleteAll();
    }
}
