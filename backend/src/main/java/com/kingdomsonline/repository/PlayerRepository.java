package com.kingdomsonline.repository;

import com.kingdomsonline.dto.OnlinePlayerDto;
import com.kingdomsonline.model.Alliance;
import com.kingdomsonline.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByEmail(String email);
    boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE Player p SET p.lastActiveAt = :time WHERE p.id = :id")
    void updateLastActiveById(Long id, LocalDateTime time);

    @Query("""
    SELECT new com.kingdomsonline.dto.OnlinePlayerDto(p.username, k.honor)
    FROM Player p
    JOIN Kingdom k ON k.player = p
    WHERE p.lastActiveAt > :since
""")
    List<OnlinePlayerDto> findOnlinePlayerSummaries(LocalDateTime since);

    long countByAllianceId(Long allianceId);
    List<Player> findByAlliance(Alliance alliance);

}
