package com.kingdomsonline.repository;

import com.kingdomsonline.model.Kingdom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KingdomRepository extends JpaRepository<Kingdom, Long> {
    Optional<Kingdom> findByPlayerId(Long playerId);
    boolean existsByXAndY(int x, int y);
}
