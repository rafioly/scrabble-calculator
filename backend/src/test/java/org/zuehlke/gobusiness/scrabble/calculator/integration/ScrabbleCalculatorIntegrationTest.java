package org.zuehlke.gobusiness.scrabble.calculator.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.zuehlke.gobusiness.scrabble.calculator.domain.LeaderboardEntry;
import org.zuehlke.gobusiness.scrabble.calculator.dto.ApiResponseDto;
import org.zuehlke.gobusiness.scrabble.calculator.dto.LeaderboardEntryDto;
import org.zuehlke.gobusiness.scrabble.calculator.dto.WordSubmissionDto;
import org.zuehlke.gobusiness.scrabble.calculator.repository.LeaderboardRepository;
import org.zuehlke.gobusiness.scrabble.calculator.service.DictionaryService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Full-Stack Integration Tests")
class ScrabbleCalculatorIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.2-alpine");

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private LeaderboardRepository leaderboardRepository;

    @MockBean
    private DictionaryService dictionaryService;

    @AfterEach
    void cleanup() {
        leaderboardRepository.deleteAll();
    }

    @Test
    @DisplayName("POST /api/leaderboards should save word and return 200 OK")
    void postWord_savesAndReturnsLeaderboardEntry() {
        // Arrange
        WordSubmissionDto submission = new WordSubmissionDto("hello");
        when(dictionaryService.isValidWord(anyString())).thenReturn(true);

        // Act
        ResponseEntity<ApiResponseDto<LeaderboardEntryDto>> response = restTemplate.exchange(
                "/api/leaderboards",
                HttpMethod.POST,
                new HttpEntity<>(submission),
                new ParameterizedTypeReference<>() {}
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("HELLO", response.getBody().getData().word());

        List<LeaderboardEntry> entries = leaderboardRepository.findAll();
        assertEquals(1, entries.size());
        assertEquals("HELLO", entries.getFirst().getWord());
    }

    @Test
    @DisplayName("POST /api/leaderboards should return 400 Bad Request for invalid English word")
    void postWord_whenWordIsInvalid_returnsBadRequest() {
        // Arrange
        WordSubmissionDto submission = new WordSubmissionDto("notarealword");
        when(dictionaryService.isValidWord("NOTAREALWORD")).thenReturn(false);

        // Act
        ResponseEntity<ApiResponseDto<Object>> response = restTemplate.exchange(
                "/api/leaderboards",
                HttpMethod.POST,
                new HttpEntity<>(submission),
                new ParameterizedTypeReference<>() {}
        );

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("not a valid English word"));

        // Assert that nothing was saved to the database
        assertEquals(0, leaderboardRepository.count());
    }

    @Test
    @DisplayName("GET /api/leaderboards should return a correctly sorted list of entries")
    void getLeaderboard_returnsSortedEntries() {
        // Arrange
        when(dictionaryService.isValidWord(anyString())).thenReturn(true);
        restTemplate.postForEntity("/api/leaderboards", new WordSubmissionDto("cabbage"), ApiResponseDto.class);
        restTemplate.postForEntity("/api/leaderboards", new WordSubmissionDto("zulu"), ApiResponseDto.class);

        // Act
        ResponseEntity<ApiResponseDto<List<LeaderboardEntryDto>>> response = restTemplate.exchange(
                "/api/leaderboards",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<LeaderboardEntryDto> leaderboard = Objects.requireNonNull(response.getBody()).getData();
        assertEquals(2, leaderboard.size());
        assertEquals("CABBAGE", leaderboard.getFirst().word());
        assertEquals(Optional.of(1), leaderboard.getFirst().rank());
    }

    @Test
    @DisplayName("GET /api/scoring-rules should return the complete map of letter scores")
    void getScoringRules_returnsMap() {
        ResponseEntity<ApiResponseDto<Map<Character, Integer>>> response = restTemplate.exchange(
                "/api/scoring-rules",
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        for (char c = 'A'; c <= 'Z'; c++) {
            assertTrue(Objects.requireNonNull(response.getBody()).getData().containsKey(c), "Missing character: " + c);
        }
    }
}
