package com.kingdomsonline.repository;

import com.kingdomsonline.model.AllianceJoinRequest;
import com.kingdomsonline.model.Player;
import com.kingdomsonline.model.Alliance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AllianceJoinRequestRepository extends JpaRepository<AllianceJoinRequest, Long> {
    boolean existsByPlayerAndAlliance(Player player, Alliance alliance);
    Optional<AllianceJoinRequest> findByPlayerAndAlliance(Player player, Alliance alliance);
    List<AllianceJoinRequest> findByAllianceAndStatus(Alliance alliance, AllianceJoinRequest.Status status);
    void deleteByAlliance(Alliance alliance);

}
