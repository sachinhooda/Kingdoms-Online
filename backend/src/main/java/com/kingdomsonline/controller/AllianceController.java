package com.kingdomsonline.controller;

import com.kingdomsonline.dto.*;
import com.kingdomsonline.service.AllianceService;
import com.kingdomsonline.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alliance")
public class AllianceController {

    private final AllianceService allianceService;
    private final JwtService jwtService;

    public AllianceController(AllianceService allianceService, JwtService jwtService) {
        this.allianceService = allianceService;
        this.jwtService = jwtService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<AllianceResponseDto>> createAlliance(
        HttpServletRequest request,
        @RequestBody CreateAllianceRequest req
    ) {
        Long playerId = jwtService.extractPlayerIdFromRequest(request);
        AllianceResponseDto dto = allianceService.createAlliance(playerId, req);
        return ResponseEntity.ok(ApiResponse.ok("Alliance created", dto));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AllianceResponseDto>> getMyAlliance(HttpServletRequest request) {
        Long playerId = jwtService.extractPlayerIdFromRequest(request);
        AllianceResponseDto dto = allianceService.getMyAlliance(playerId);

        if (dto == null) {
            return ResponseEntity.ok(ApiResponse.ok("Not part of any alliance", null));
        }

        return ResponseEntity.ok(ApiResponse.ok("Alliance details", dto));
    }
    @PostMapping("/leave")
    public ResponseEntity<ApiResponse<Void>> leaveAlliance(HttpServletRequest request) {
        Long playerId = jwtService.extractPlayerIdFromRequest(request);
        allianceService.leaveAlliance(playerId);
        return ResponseEntity.ok(ApiResponse.ok("Left alliance", null));
    }
    @GetMapping
    public ResponseEntity<ApiResponse<Page<AllianceResponseDto>>> getAll(Pageable pageable) {
        Page<AllianceResponseDto> page = allianceService.getAllAlliances(pageable);
        return ResponseEntity.ok(ApiResponse.ok("All alliances", page));
    }
    @PostMapping("/request-join")
    public ResponseEntity<ApiResponse<Void>> requestToJoin(
            @RequestBody JoinAllianceRequest req,
            HttpServletRequest request) {

        Long playerId = jwtService.extractPlayerIdFromRequest(request);
        allianceService.requestToJoin(playerId, req.allianceId());

        return ResponseEntity.ok(ApiResponse.ok("Join request submitted", null));
    }

    @GetMapping("/requests")
    public ResponseEntity<ApiResponse<List<JoinRequestResponseDto>>> getJoinRequests(HttpServletRequest request) {
        Long playerId = jwtService.extractPlayerIdFromRequest(request);
        List<JoinRequestResponseDto> list = allianceService.getPendingRequests(playerId);
        return ResponseEntity.ok(ApiResponse.ok("Pending join requests", list));
    }
    @PostMapping("/respond")
    public ResponseEntity<ApiResponse<Void>> respondToRequest(
            @RequestBody RespondJoinRequestDto req,
            HttpServletRequest request) {
        Long leaderId = jwtService.extractPlayerIdFromRequest(request);
        allianceService.respondToJoinRequest(leaderId, req.requestId(), req.approve());
        return ResponseEntity.ok(ApiResponse.ok("Request handled", null));
    }


    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<AllianceResponseDto>>> search(
            @RequestParam String query,
            Pageable pageable) {
        Page<AllianceResponseDto> result = allianceService.searchAlliances(query, pageable);
        return ResponseEntity.ok(ApiResponse.ok("Search results", result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AllianceResponseDto>> getById(@PathVariable Long id) {
        AllianceResponseDto dto = allianceService.getAllianceById(id);
        return ResponseEntity.ok(ApiResponse.ok("Alliance found", dto));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteAlliance(HttpServletRequest request) {
        Long leaderId = jwtService.extractPlayerIdFromRequest(request);
        allianceService.deleteAlliance(leaderId);
        return ResponseEntity.ok(ApiResponse.ok("Alliance deleted successfully", null));
    }


}
