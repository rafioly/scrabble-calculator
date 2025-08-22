package org.zuehlke.gobusiness.scrabble.calculator.service;

import java.util.Map;

public interface ScoringService {
    Map<Character, Integer> getLetterScores();
    int calculateScore(String word);
}