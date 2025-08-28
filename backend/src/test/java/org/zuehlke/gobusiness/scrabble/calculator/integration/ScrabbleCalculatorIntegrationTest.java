package org.zuehlke.gobusiness.scrabble.calculator.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import org.zuehlke.gobusiness.scrabble.calculator.dto.ApiResponse;
import org.zuehlke.gobusiness.scrabble.calculator.dto.LeaderboardEntryDto;
import org.zuehlke.gobusiness.scrabble.calculator.dto.WordSubmissionDto;
import org.zuehlke.gobusiness.scrabble.calculator.repository.LeaderboardRepository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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

    @AfterEach
    void cleanup() {
        leaderboardRepository.deleteAll();
    }

    @Test
    @DisplayName("POST /api/leaderboards should save word and return 200 OK")
    void postWord_savesAndReturnsLeaderboardEntry() {
        // Arrange
        WordSubmissionDto submission = new WordSubmissionDto("hello");

        // Act: Make the API call
        ResponseEntity<ApiResponse<LeaderboardEntryDto>> response = restTemplate.exchange(
                "/api/leaderboards",
                HttpMethod.POST,
                new HttpEntity<>(submission),
                new ParameterizedTypeReference<>() {}
        );

        // Assert on the API Response
        assertEquals(HttpStatus.OK, response.getStatusCode()); // Expect 200 OK
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("hello", response.getBody().getData().word());
        assertNull(response.getHeaders().getLocation()); // Verify no Location header

        // Assert on the Database State
        List<LeaderboardEntry> entries = leaderboardRepository.findAll();
        assertEquals(1, entries.size());
        assertEquals("hello", entries.getFirst().getWord());
        assertEquals(8, entries.getFirst().getScore());
    }

    @Test
    @DisplayName("GET /api/leaderboards should return a correctly sorted list of entries")
    void getLeaderboard_returnsSortedEntries() {
        // Arrange: Save some scores
        restTemplate.postForEntity("/api/leaderboards", new WordSubmissionDto("cabbage"), ApiResponse.class);
        restTemplate.postForEntity("/api/leaderboards", new WordSubmissionDto("zulu"), ApiResponse.class);

        // Act: Get the leaderboard
        ResponseEntity<ApiResponse<List<LeaderboardEntryDto>>> response = restTemplate.exchange(
                "/api/leaderboards",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<LeaderboardEntryDto> leaderboard = Objects.requireNonNull(response.getBody()).getData();
        assertEquals(2, leaderboard.size());
        assertEquals("cabbage", leaderboard.getFirst().word());
        assertEquals(Optional.of(1), leaderboard.getFirst().rank());
    }

    @Test
    @DisplayName("GET /api/scoring-rules should return the complete map of letter scores")
    void getScoringRules_returnsMap() {
        ResponseEntity<ApiResponse<Map<Character, Integer>>> response = restTemplate.exchange(
                "/api/scoring-rules",
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        for (char c = 'A'; c <= 'Z'; c++) {
            assertTrue(Objects.requireNonNull(response.getBody()).getData().containsKey(c), "Missing character: " + c);
        }

    }
}
