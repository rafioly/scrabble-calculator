package org.zuehlke.gobusiness.scrabble.calculator.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zuehlke.gobusiness.scrabble.calculator.dto.ApiResponse;
import org.zuehlke.gobusiness.scrabble.calculator.service.ScoringService;

import java.util.Map;

@RestController
@RequestMapping("/api/scoring-rules")
@RequiredArgsConstructor
public class ScoringController {

    private final ScoringService scoringService;

    @GetMapping
    public ResponseEntity<ApiResponse<Map<Character, Integer>>> getScoringRules() {
        return ResponseEntity.ok(ApiResponse.success(scoringService.getLetterScores()));
    }
}
