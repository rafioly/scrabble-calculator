package org.zuehlke.gobusiness.scrabble.calculator.service.impl;

import org.springframework.stereotype.Service;
import org.zuehlke.gobusiness.scrabble.calculator.service.ScoringService;

import java.util.Map;
import java.util.TreeMap;

@Service
public class ScoringServiceImpl implements ScoringService {

    private static final Map<Character, Integer> LETTER_SCORES = Map.ofEntries(
            Map.entry('A', 1), Map.entry('B', 3), Map.entry('C', 3), Map.entry('D', 2), Map.entry('E', 1),
            Map.entry('F', 4), Map.entry('G', 2), Map.entry('H', 4), Map.entry('I', 1), Map.entry('J', 8),
            Map.entry('K', 5), Map.entry('L', 1), Map.entry('M', 3), Map.entry('N', 1), Map.entry('O', 1),
            Map.entry('P', 3), Map.entry('Q', 10), Map.entry('R', 1), Map.entry('S', 1), Map.entry('T', 1),
            Map.entry('U', 1), Map.entry('V', 4), Map.entry('W', 4), Map.entry('X', 8), Map.entry('Y', 4),
            Map.entry('Z', 10)
    );

    @Override
    public Map<Character, Integer> getLetterScores() {
        return new TreeMap<>(LETTER_SCORES);
    }

    @Override
    public int calculateScore(String word) {
        return word.toUpperCase().chars()
                .map(c -> LETTER_SCORES.getOrDefault((char) c, 0))
                .sum();
    }
}