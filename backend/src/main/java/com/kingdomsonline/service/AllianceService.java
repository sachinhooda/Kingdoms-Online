package com.kingdomsonline.service;

import com.kingdomsonline.dto.AllianceResponseDto;
import com.kingdomsonline.dto.CreateAllianceRequest;
import com.kingdomsonline.dto.JoinRequestResponseDto;
import com.kingdomsonline.model.Alliance;
import com.kingdomsonline.model.AllianceJoinRequest;
import com.kingdomsonline.model.Player;
import com.kingdomsonline.repository.AllianceJoinRequestRepository;
import com.kingdomsonline.repository.AllianceRepository;
import com.kingdomsonline.repository.PlayerRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AllianceService {

    private final AllianceRepository allianceRepository;
    private final PlayerRepository playerRepository;
    private final AllianceJoinRequestRepository joinRequestRepository;

    public AllianceService(AllianceRepository allianceRepository, PlayerRepository playerRepository, AllianceJoinRequestRepository allianceJoinRequestRepository) {
        this.allianceRepository = allianceRepository;
        this.playerRepository = playerRepository;
        this.joinRequestRepository = allianceJoinRequestRepository;
    }

    public AllianceResponseDto createAlliance(Long playerId, CreateAllianceRequest req) {
        if (allianceRepository.existsByName(req.name()))
            throw new IllegalArgumentException("Alliance name already exists");

        if (allianceRepository.existsByTag(req.tag()))
            throw new IllegalArgumentException("Alliance tag already exists");

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        if (player.getAlliance() != null)
            throw new IllegalStateException("Player already in an alliance");

        Alliance alliance = new Alliance();
        alliance.setName(req.name());
        alliance.setTag(req.tag());
        alliance.setDescription(req.description());
        alliance.setCreatedAt(LocalDateTime.now());
        alliance.setFounder(player);
        alliance.setLeader(player);

        Alliance saved = allianceRepository.save(alliance);
        player.setAlliance(saved);
        playerRepository.save(player);

        return new AllianceResponseDto(
            saved.getId(),
            saved.getName(),
            saved.getTag(),
            saved.getDescription(),
            player.getUsername(),
            player.getUsername()
        );
    }

    public AllianceResponseDto getMyAlliance(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        Alliance alliance = player.getAlliance();
        if (alliance == null) return null;

        return new AllianceResponseDto(
                alliance.getId(),
                alliance.getName(),
                alliance.getTag(),
                alliance.getDescription(),
                alliance.getFounder().getUsername(),
                alliance.getLeader().getUsername()
        );
    }

    @Transactional
    public void leaveAlliance(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        Alliance alliance = player.getAlliance();
        if (alliance == null) {
            throw new RuntimeException("You are not part of any alliance.");
        }

        if (alliance.getLeader().getId().equals(playerId)) {
            throw new RuntimeException("You are the leader. Transfer leadership before leaving.");
        }

        long memberCount = playerRepository.countByAllianceId(alliance.getId());
        if (memberCount == 1) {
            allianceRepository.delete(alliance);
        }

        player.setAlliance(null);
        playerRepository.save(player);
    }

    public Page<AllianceResponseDto> getAllAlliances(Pageable pageable) {
        return allianceRepository.findByIsDeletedFalse(pageable)
                .map(alliance -> new AllianceResponseDto(
                        alliance.getId(),
                        alliance.getName(),
                        alliance.getTag(),
                        alliance.getDescription(),
                        alliance.getFounder().getUsername(),
                        alliance.getLeader().getUsername()
                ));
    }

    @Transactional
    public void requestToJoin(Long playerId, Long allianceId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        if (player.getAlliance() != null) {
            throw new RuntimeException("Leave your current alliance before joining another.");
        }

        Alliance alliance = allianceRepository.findById(allianceId)
                .orElseThrow(() -> new RuntimeException("Alliance not found"));

        boolean alreadyRequested = joinRequestRepository.existsByPlayerAndAlliance(player, alliance);
        if (alreadyRequested) {
            throw new RuntimeException("You have already requested to join this alliance.");
        }

        AllianceJoinRequest request = new AllianceJoinRequest();
        request.setPlayer(player);
        request.setAlliance(alliance);
        request.setRequestedAt(LocalDateTime.now());
        request.setStatus(AllianceJoinRequest.Status.PENDING);

        joinRequestRepository.save(request);
    }

    public List<JoinRequestResponseDto> getPendingRequests(Long leaderId) {
        Player leader = playerRepository.findById(leaderId)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        Alliance alliance = leader.getAlliance();
        if (alliance == null || !alliance.getLeader().getId().equals(leaderId)) {
            throw new RuntimeException("You are not the leader of any alliance.");
        }

        return joinRequestRepository.findByAllianceAndStatus(alliance, AllianceJoinRequest.Status.PENDING)
                .stream()
                .map(req -> new JoinRequestResponseDto(
                        req.getId(),
                        req.getPlayer().getId(),
                        req.getPlayer().getUsername(),
                        req.getRequestedAt()
                )).toList();
    }
    @Transactional
    public void respondToJoinRequest(Long leaderId, Long requestId, boolean approve) {
        Player leader = playerRepository.findById(leaderId)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        Alliance alliance = leader.getAlliance();
        if (alliance == null || !alliance.getLeader().getId().equals(leaderId)) {
            throw new RuntimeException("You are not authorized to respond.");
        }

        AllianceJoinRequest request = joinRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Join request not found"));

        if (!request.getAlliance().getId().equals(alliance.getId())) {
            throw new RuntimeException("This request does not belong to your alliance.");
        }

        if (request.getStatus() != AllianceJoinRequest.Status.PENDING) {
            throw new RuntimeException("This request has already been handled.");
        }

        if (approve) {
            Player joiningPlayer = request.getPlayer();
            if (joiningPlayer.getAlliance() != null) {
                throw new RuntimeException("Player has already joined another alliance.");
            }
            joiningPlayer.setAlliance(alliance);
            playerRepository.save(joiningPlayer);
            request.setStatus(AllianceJoinRequest.Status.APPROVED);
        } else {
            request.setStatus(AllianceJoinRequest.Status.REJECTED);
        }

        joinRequestRepository.save(request);
    }
    public Page<AllianceResponseDto> searchAlliances(String query, Pageable pageable) {
        return allianceRepository
                .findByNameContainingIgnoreCaseOrTagContainingIgnoreCaseAndIsDeletedFalse(query, query, pageable)
                .map(alliance -> new AllianceResponseDto(
                        alliance.getId(),
                        alliance.getName(),
                        alliance.getTag(),
                        alliance.getDescription(),
                        alliance.getFounder().getUsername(),
                        alliance.getLeader().getUsername()
                ));
    }
    public AllianceResponseDto getAllianceById(Long id) {
        Alliance alliance = allianceRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Alliance not found"));
        return new AllianceResponseDto(
                alliance.getId(),
                alliance.getName(),
                alliance.getTag(),
                alliance.getDescription(),
                alliance.getFounder().getUsername(),
                alliance.getLeader().getUsername()
        );
    }
    @Transactional
    public void deleteAlliance(Long leaderId) {
        Player leader = playerRepository.findById(leaderId)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        Alliance alliance = leader.getAlliance();
        if (alliance == null || !alliance.getLeader().getId().equals(leaderId)) {
            throw new RuntimeException("Only the alliance leader can delete the alliance.");
        }

        // Remove join requests
        joinRequestRepository.deleteByAlliance(alliance);

        // Remove all members from the alliance
        List<Player> members = playerRepository.findByAlliance(alliance);
        for (Player member : members) {
            member.setAlliance(null);
        }
        playerRepository.saveAll(members);

        // Mark alliance as soft-deleted
        alliance.setDeleted(true);

        allianceRepository.save(alliance);
    }


}
