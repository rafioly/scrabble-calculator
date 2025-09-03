package org.zuehlke.gobusiness.scrabble.calculator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zuehlke.gobusiness.scrabble.calculator.dto.ApiResponseDto;
import org.zuehlke.gobusiness.scrabble.calculator.service.ScoringService;

import java.util.Map;

@RestController
@RequestMapping("/api/scoring-rules")
@RequiredArgsConstructor
@Tag(name = "Scoring Rules", description = "API for retrieving Scrabble letter scores.")
public class ScoringController {

    private final ScoringService scoringService;

    @Operation(summary = "Get scoring rules", description = "Retrieves the complete map of letters to their point values.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Scoring rules retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<ApiResponseDto<Map<Character, Integer>>> getScoringRules() {
        return ResponseEntity.ok(ApiResponseDto.success(scoringService.getLetterScores()));
    }
}
