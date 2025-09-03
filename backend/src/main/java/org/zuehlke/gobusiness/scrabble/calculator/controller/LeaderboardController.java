package org.zuehlke.gobusiness.scrabble.calculator.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zuehlke.gobusiness.scrabble.calculator.dto.ApiResponse;
import org.zuehlke.gobusiness.scrabble.calculator.dto.LeaderboardEntryDto;
import org.zuehlke.gobusiness.scrabble.calculator.dto.WordSubmissionDto;
import org.zuehlke.gobusiness.scrabble.calculator.service.LeaderboardService;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboards")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @PostMapping
    public ResponseEntity<ApiResponse<LeaderboardEntryDto>> saveScore(@Valid @RequestBody WordSubmissionDto wordSubmission) {
        LeaderboardEntryDto newScore = leaderboardService.saveNewScore(wordSubmission.word());
        return ResponseEntity.ok(ApiResponse.success(newScore, "New score saved successfully."));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LeaderboardEntryDto>>> getLeaderboard(@RequestParam(defaultValue = "10") int top) {
        return ResponseEntity.ok(ApiResponse.success(leaderboardService.getLeaderboard(top)));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllScores() {
        leaderboardService.deleteAllScores();
        return ResponseEntity.noContent().build();
    }
}
