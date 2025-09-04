package org.zuehlke.gobusiness.scrabble.calculator.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.zuehlke.gobusiness.scrabble.calculator.controller.LeaderboardController;
import org.zuehlke.gobusiness.scrabble.calculator.dto.LeaderboardEntryDto;
import org.zuehlke.gobusiness.scrabble.calculator.dto.WordSubmissionDto;
import org.zuehlke.gobusiness.scrabble.calculator.exception.GlobalExceptionHandler;
import org.zuehlke.gobusiness.scrabble.calculator.exception.InvalidWordException;
import org.zuehlke.gobusiness.scrabble.calculator.service.LeaderboardService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({LeaderboardController.class, GlobalExceptionHandler.class})
@DisplayName("Unit Tests for Leaderboard API Controller")
class LeaderboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LeaderboardService leaderboardService;

    @Test
    @DisplayName("POST /api/leaderboards should return 200 OK with the new score")
    void saveScore_whenCalledWithWord_returnsOkResponse() throws Exception {
        WordSubmissionDto submission = new WordSubmissionDto("test");
        LeaderboardEntryDto expectedDto = new LeaderboardEntryDto(Optional.empty(), "test", 4);

        when(leaderboardService.saveNewScore(anyString())).thenReturn(expectedDto);

        mockMvc.perform(post("/api/leaderboards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(submission)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.word").value("test"));
    }

    @Test
    @DisplayName("POST /api/leaderboards should return 400 Bad Request when word is invalid")
    void saveScore_whenWordIsInvalid_returns400BadRequest() throws Exception {
        WordSubmissionDto submission = new WordSubmissionDto("invalid123");
        String errorMessage = "Word must only contain alphabetic characters.";

        when(leaderboardService.saveNewScore(anyString()))
                .thenThrow(new InvalidWordException(errorMessage));

        mockMvc.perform(post("/api/leaderboards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(submission)))
                .andExpect(status().isBadRequest()) // Expect 400 Bad Request
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

    @Test
    @DisplayName("POST /api/leaderboards should return 409 Conflict when word already exists")
    void saveScore_whenWordExists_returns409Conflict() throws Exception {
        WordSubmissionDto submission = new WordSubmissionDto("duplicate");

        when(leaderboardService.saveNewScore(anyString()))
                .thenThrow(new DataIntegrityViolationException("Duplicate entry for key 'word'"));

        mockMvc.perform(post("/api/leaderboards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(submission)))
                .andExpect(status().isConflict()) // Expect 409 Conflict
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("This word already exists on the leaderboard. Please submit a unique word."));
    }

    @Test
    @DisplayName("GET /api/leaderboards should return the leaderboard data from the service")
    void getLeaderboard_whenCalled_returnsLeaderboard() throws Exception {
        List<LeaderboardEntryDto> leaderboard = List.of(
                new LeaderboardEntryDto(Optional.of(1), "word1", 10),
                new LeaderboardEntryDto(Optional.of(2), "word2", 5)
        );

        when(leaderboardService.getLeaderboard(anyInt())).thenReturn(leaderboard);

        mockMvc.perform(get("/api/leaderboards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].word").value("word1"));
    }
}
