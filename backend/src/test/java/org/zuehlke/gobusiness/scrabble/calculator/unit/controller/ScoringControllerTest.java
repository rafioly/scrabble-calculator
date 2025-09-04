package org.zuehlke.gobusiness.scrabble.calculator.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.zuehlke.gobusiness.scrabble.calculator.controller.ScoringController;
import org.zuehlke.gobusiness.scrabble.calculator.exception.GlobalExceptionHandler;
import org.zuehlke.gobusiness.scrabble.calculator.service.ScoringService;

import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ScoringController.class, GlobalExceptionHandler.class})
@DisplayName("Unit Tests for Scoring API Controller")
class ScoringControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ScoringService scoringService;

    @Test
    @DisplayName("GET /api/scoring-rules should return the map of letter scores")
    void getScoringRules_returnsScoreMap() throws Exception {
        when(scoringService.getLetterScores()).thenReturn(Map.of('A', 1, 'B', 3));

        mockMvc.perform(get("/api/scoring-rules"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.A").value(1));
    }
}
