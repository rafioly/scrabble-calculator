package org.zuehlke.gobusiness.scrabble.calculator.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zuehlke.gobusiness.scrabble.calculator.dto.ApiResponse;
import org.zuehlke.gobusiness.scrabble.calculator.dto.LeaderboardEntryDto;
import org.zuehlke.gobusiness.scrabble.calculator.dto.WordSubmissionDto;
import org.zuehlke.gobusiness.scrabble.calculator.service.LeaderboardService;
import org.zuehlke.gobusiness.scrabble.calculator.service.ScoringService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ScrabbleCalculatorController {

    private final ScoringService scoringService;
    private final LeaderboardService leaderboardService;

    @GetMapping("/scoring-rules")
    public ResponseEntity<ApiResponse<Map<Character, Integer>>> getScoringRules() {
        // Wrap the successful response in ResponseEntity.ok()
        return ResponseEntity.ok(ApiResponse.success(scoringService.getLetterScores()));
    }

    @PostMapping("/leaderboards")
    public ResponseEntity<ApiResponse<LeaderboardEntryDto>> saveScore(@Valid @RequestBody WordSubmissionDto wordSubmission) {
        LeaderboardEntryDto newScore = leaderboardService.saveNewScore(wordSubmission.word());
        // Return 200 OK with the new score in the body. No Location header.
        return ResponseEntity.ok(ApiResponse.success(newScore, "New score saved successfully."));
    }

    @GetMapping("/leaderboards")
    public ResponseEntity<ApiResponse<List<LeaderboardEntryDto>>> getLeaderboard(@RequestParam(defaultValue = "10") int top) {
        // Wrap the successful response in ResponseEntity.ok()
        return ResponseEntity.ok(ApiResponse.success(leaderboardService.getLeaderboard(top)));
    }
}
