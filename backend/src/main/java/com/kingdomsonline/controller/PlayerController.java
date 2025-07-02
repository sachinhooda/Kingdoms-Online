package com.kingdomsonline.controller;

import com.kingdomsonline.dto.*;
import com.kingdomsonline.service.JwtService;
import com.kingdomsonline.service.PlayerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/player")
public class PlayerController {

    private final PlayerService playerService;
    private final JwtService jwtService;

    public PlayerController(PlayerService playerService, JwtService jwtService) {
        this.playerService = playerService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody PlayerRegistrationRequest request) {
        ApiResponse<Void> response = playerService.register(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        ApiResponse<LoginResponse> response = playerService.login(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        ApiResponse<Void> response = playerService.forgotPassword(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestBody ResetPasswordRequest request) {
        ApiResponse<Void> response = playerService.resetPassword(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@RequestParam("token") String token) {
        ApiResponse<Void> response = playerService.verifyEmail(token);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/heartbeat")
    public ResponseEntity<ApiResponse<Void>> heartbeat(HttpServletRequest request) {
        Long playerId = jwtService.extractPlayerIdFromRequest(request);
        playerService.recordHeartbeat(playerId);
        return ResponseEntity.ok(ApiResponse.ok("Heartbeat recorded", null));
    }

    @GetMapping("/online")
    public ResponseEntity<ApiResponse<List<OnlinePlayerDto>>> getOnlinePlayers() {
        List<OnlinePlayerDto> onlinePlayers = playerService.getOnlinePlayers();
        return ResponseEntity.ok(ApiResponse.ok("Online players fetched", onlinePlayers));
    }


}
