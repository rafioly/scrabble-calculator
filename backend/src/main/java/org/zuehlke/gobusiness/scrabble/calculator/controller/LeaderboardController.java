package org.zuehlke.gobusiness.scrabble.calculator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zuehlke.gobusiness.scrabble.calculator.dto.ApiResponseDto;
import org.zuehlke.gobusiness.scrabble.calculator.dto.LeaderboardEntryDto;
import org.zuehlke.gobusiness.scrabble.calculator.dto.WordSubmissionDto;
import org.zuehlke.gobusiness.scrabble.calculator.service.LeaderboardService;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboards")
@RequiredArgsConstructor
@Tag(name = "Leaderboard Management", description = "APIs for managing and retrieving leaderboard scores.")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @Operation(summary = "Submit a new score", description = "Submits a word, calculates its score, and saves it to the leaderboard.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Score saved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input, e.g., word contains numbers or is empty")
    })
    @PostMapping
    public ResponseEntity<ApiResponseDto<LeaderboardEntryDto>> saveScore(@Valid @RequestBody WordSubmissionDto wordSubmission) {
        LeaderboardEntryDto newScore = leaderboardService.saveNewScore(wordSubmission.word());
        return ResponseEntity.ok(ApiResponseDto.success(newScore, "New score saved successfully."));
    }

    @Operation(summary = "Get top scores", description = "Retrieves a list of the top scores from the leaderboard.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leaderboard retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<LeaderboardEntryDto>>> getLeaderboard(@RequestParam(defaultValue = "10") int top) {
        return ResponseEntity.ok(ApiResponseDto.success(leaderboardService.getLeaderboard(top)));
    }

    @Operation(summary = "Delete all scores", description = "Deletes all entries from the leaderboard.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "All scores successfully deleted")
    })
    @DeleteMapping
    public ResponseEntity<Void> deleteAllScores() {
        leaderboardService.deleteAllScores();
        return ResponseEntity.noContent().build();
    }
}
