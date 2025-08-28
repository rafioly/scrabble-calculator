package org.zuehlke.gobusiness.scrabble.calculator.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "leaderboard", indexes = {
    @Index(name = "idx_score", columnList = "score")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String word;

    private int score;

    public LeaderboardEntry(String word, int score) {
        this.word = word;
        this.score = score;
    }
}
