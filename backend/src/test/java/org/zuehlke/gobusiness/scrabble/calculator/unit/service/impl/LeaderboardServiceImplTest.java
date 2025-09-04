package org.zuehlke.gobusiness.scrabble.calculator.unit.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.zuehlke.gobusiness.scrabble.calculator.domain.LeaderboardEntry;
import org.zuehlke.gobusiness.scrabble.calculator.dto.LeaderboardEntryDto;
import org.zuehlke.gobusiness.scrabble.calculator.exception.InvalidWordException;
import org.zuehlke.gobusiness.scrabble.calculator.repository.LeaderboardRepository;
import org.zuehlke.gobusiness.scrabble.calculator.service.DictionaryService;
import org.zuehlke.gobusiness.scrabble.calculator.service.ScoringService;
import org.zuehlke.gobusiness.scrabble.calculator.validator.WordValidator;
import org.zuehlke.gobusiness.scrabble.calculator.service.impl.LeaderboardServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Tests for Leaderboard Service Logic")
class LeaderboardServiceImplTest {

    @Mock
    private LeaderboardRepository leaderboardRepository;
    @Mock
    private ScoringService scoringService;
    @Mock
    private WordValidator validator;
    @Mock
    private DictionaryService dictionaryService;

    @InjectMocks
    private LeaderboardServiceImpl leaderboardService;

    @Test
    @DisplayName("Should calculate score and save the entry when a new score is submitted")
    void saveNewScore_calculatesScoreAndSavesEntry() {
        String word = "test";
        String normalizedWord = normalizeWord(word);
        int expectedScore = 4;

        when(dictionaryService.isValidWord(normalizedWord)).thenReturn(true);
        when(scoringService.calculateScore(normalizedWord)).thenReturn(expectedScore);
        when(leaderboardRepository.save(any(LeaderboardEntry.class))).thenReturn(new LeaderboardEntry());
        doNothing().when(validator).validate(anyString());

        LeaderboardEntryDto resultDto = leaderboardService.saveNewScore(word);

        verify(dictionaryService).isValidWord(normalizedWord);
        verify(scoringService).calculateScore(normalizedWord);
        verify(leaderboardRepository).save(argThat(entry ->
                entry.getWord().equals(normalizedWord) && entry.getScore() == expectedScore
        ));
        assertNotNull(resultDto);
        assertEquals(word.toUpperCase(), resultDto.word());
        assertEquals(expectedScore, resultDto.score());
        assertTrue(resultDto.rank().isEmpty());
    }

    @Test
    @DisplayName("Should throw exception for structurally invalid words")
    void saveNewScore_invalidWord_throwsException() {
        String word = "invalid123";
        String normalizedWord = normalizeWord(word);
        doThrow(InvalidWordException.class).when(validator).validate(normalizedWord);

        assertThrows(InvalidWordException.class, () -> leaderboardService.saveNewScore(word));
        verify(dictionaryService, never()).isValidWord(anyString());
    }

    @Test
    @DisplayName("Should throw exception when word is not a valid English word")
    void saveNewScore_whenWordIsNotAValidEnglishWord_throwsException() {
        String word = "notaword";
        String normalizedWord = normalizeWord(word);
        doNothing().when(validator).validate(normalizedWord);
        when(dictionaryService.isValidWord(normalizedWord)).thenReturn(false);

        assertThrows(InvalidWordException.class, () -> leaderboardService.saveNewScore(word));
        verify(scoringService, never()).calculateScore(anyString());
        verify(leaderboardRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should fetch entries and correctly assign ranks for the leaderboard")
    void getLeaderboard_returnsRankedEntries() {
        LeaderboardEntry entry1 = new LeaderboardEntry("word1", 10);
        LeaderboardEntry entry2 = new LeaderboardEntry("word2", 5);
        List<LeaderboardEntry> entries = List.of(entry1, entry2);

        when(leaderboardRepository.findByOrderByScoreDesc(PageRequest.of(0, 2))).thenReturn(entries);

        List<LeaderboardEntryDto> result = leaderboardService.getLeaderboard(2);

        assertEquals(2, result.size());
        assertEquals(new LeaderboardEntryDto(Optional.of(1), "word1", 10), result.get(0));
        assertEquals(new LeaderboardEntryDto(Optional.of(2), "word2", 5), result.get(1));
    }

    private String normalizeWord(String word) {
        return word.trim().toUpperCase();
    }
}
