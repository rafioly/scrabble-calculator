package org.zuehlke.gobusiness.scrabble.calculator.unit.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.zuehlke.gobusiness.scrabble.calculator.service.ScoringService;
import org.zuehlke.gobusiness.scrabble.calculator.service.impl.ScoringServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Unit Tests for Scoring Service Logic")
class ScoringServiceImplTest {

    private ScoringService scoringService;

    @BeforeEach
    void setUp() {
        scoringService = new ScoringServiceImpl();
    }

    @Test
    @DisplayName("Should return the correct score for a simple, valid word")
    void calculateScore_withValidWord_returnsCorrectScore() {
        assertEquals(8, scoringService.calculateScore("hello"));
    }

    @Test
    @DisplayName("Should correctly score a word with mixed uppercase and lowercase letters")
    void calculateScore_withMixedCaseWord_returnsCorrectScore() {
        assertEquals(14, scoringService.calculateScore("Java"));
    }

    @Test
    @DisplayName("Should ignore non-alphabetic characters and spaces when calculating score")
    void calculateScore_withInvalidCharacters_ignoresThem() {
        assertEquals(8, scoringService.calculateScore("he!l lo"));
    }

    @Test
    @DisplayName("Should return a score of 0 for an empty string")
    void calculateScore_withEmptyString_returnsZero() {
        assertEquals(0, scoringService.calculateScore(""));
    }

    @Test
    @DisplayName("Should return a score of 0 for a string containing only invalid characters")
    void calculateScore_withOnlyInvalidCharacters_returnsZero() {
        assertEquals(0, scoringService.calculateScore("123!@#"));
    }
}
