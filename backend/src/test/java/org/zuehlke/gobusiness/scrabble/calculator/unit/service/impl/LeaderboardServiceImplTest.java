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
import org.zuehlke.gobusiness.scrabble.calculator.repository.LeaderboardRepository;
import org.zuehlke.gobusiness.scrabble.calculator.service.ScoringService;
import org.zuehlke.gobusiness.scrabble.calculator.service.WordValidator;
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

    @InjectMocks
    private LeaderboardServiceImpl leaderboardService;

    @Test
    @DisplayName("Should calculate score and save the entry when a new score is submitted")
    void saveNewScore_calculatesScoreAndSavesEntry() {
        String word = "test";
        int expectedScore = 4;

        when(scoringService.calculateScore(word)).thenReturn(expectedScore);
        when(leaderboardRepository.save(any(LeaderboardEntry.class))).thenReturn(new LeaderboardEntry()); // Return dummy object
        doNothing().when(validator).validate(anyString());

        LeaderboardEntryDto resultDto = leaderboardService.saveNewScore(word);

        verify(scoringService).calculateScore(word);
        verify(leaderboardRepository).save(argThat(entry ->
                entry.getWord().equals(word) && entry.getScore() == expectedScore
        ));

        // Assert on the returned DTO (no ID)
        assertNotNull(resultDto);
        assertEquals(word, resultDto.word());
        assertEquals(expectedScore, resultDto.score());
        assertTrue(resultDto.rank().isEmpty());
    }

    @Test
    @DisplayName("Should fetch entries and correctly assign ranks for the leaderboard")
    void getLeaderboard_returnsRankedEntries() {
        // Arrange: Create mock data from the repository
        LeaderboardEntry entry1 = new LeaderboardEntry("word1", 10);
        LeaderboardEntry entry2 = new LeaderboardEntry("word2", 5);
        List<LeaderboardEntry> entries = List.of(entry1, entry2);

        when(leaderboardRepository.findByOrderByScoreDesc(PageRequest.of(0, 2))).thenReturn(entries);

        // Act: Call the service method
        List<LeaderboardEntryDto> result = leaderboardService.getLeaderboard(2);

        // Assert: Check that the ranks are assigned correctly in the DTOs (no IDs)
        assertEquals(2, result.size());
        assertEquals(new LeaderboardEntryDto(Optional.of(1), "word1", 10), result.get(0));
        assertEquals(new LeaderboardEntryDto(Optional.of(2), "word2", 5), result.get(1));
    }

    // Tests for getLeaderboardEntry by ID have been removed as the method is no longer part of the service.
}
