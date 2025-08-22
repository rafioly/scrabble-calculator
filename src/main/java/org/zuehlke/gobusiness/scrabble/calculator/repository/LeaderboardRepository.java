package org.zuehlke.gobusiness.scrabble.calculator.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zuehlke.gobusiness.scrabble.calculator.domain.LeaderboardEntry;

import java.util.List;

public interface LeaderboardRepository extends JpaRepository<LeaderboardEntry, Long> {

    List<LeaderboardEntry> findByOrderByScoreDesc(Pageable pageable);

    List<LeaderboardEntry> findAllByOrderByScoreDesc();
}