package org.zuehlke.gobusiness.scrabble.calculator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zuehlke.gobusiness.scrabble.calculator.domain.LeaderboardEntry;

public interface LeaderboardRepository extends JpaRepository<LeaderboardEntry, Long> {

}