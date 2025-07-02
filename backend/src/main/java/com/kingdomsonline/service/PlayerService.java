package com.kingdomsonline.service;

import com.kingdomsonline.dto.*;
import com.kingdomsonline.model.*;
import com.kingdomsonline.repository.KingdomRepository;
import com.kingdomsonline.repository.PlayerRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final KingdomRepository kingdomRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private  final MapService mapService;
    private final Random random = new Random();

    public PlayerService(PlayerRepository playerRepository, KingdomRepository kingdomRepository,
                         PasswordEncoder passwordEncoder, JwtService jwtService, MapService mapService) {
        this.playerRepository = playerRepository;
        this.kingdomRepository = kingdomRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.mapService = mapService;
    }

    public ApiResponse<Void> register(PlayerRegistrationRequest request) {
        if (playerRepository.existsByEmail(request.email())) {
            var error = new ErrorDetail("EMAIL_EXISTS", "email", "Email is already registered.");
            return ApiResponse.fail("Registration failed", 400, List.of(error));
        }

        Player player = new Player();
        player.setUsername(request.username());
        player.setEmail(request.email());
        player.setPassword(passwordEncoder.encode(request.password()));
        player.setEmailVerificationToken(UUID.randomUUID().toString());
        playerRepository.save(player);

        sendVerificationEmail(player); // simulate

        Coordinates coords = findAvailableCoordinates(player.getId());

        Kingdom kingdom = new Kingdom();
        kingdom.setPlayer(player);
        kingdom.setX(coords.x());
        kingdom.setY(coords.y());
        kingdom.setRace(Race.HUMAN);

        kingdomRepository.save(kingdom);

        return ApiResponse.ok("Registered successfully. Please check your email to verify your account.", null);
    }

    public ApiResponse<LoginResponse> login(LoginRequest request) {
        Optional<Player> optional = playerRepository.findByEmail(request.email());
        if (optional.isEmpty()) {
            var error = new ErrorDetail("INVALID_CREDENTIALS", "email", "Email not found.");
            return ApiResponse.fail("Login failed", 401, List.of(error));
        }

        Player player = optional.get();
        if (!passwordEncoder.matches(request.password(), player.getPassword())) {
            var error = new ErrorDetail("INVALID_CREDENTIALS", "password", "Password is incorrect.");
            return ApiResponse.fail("Login failed", 401, List.of(error));
        }

        if (!player.isEmailVerified()) {
            var error = new ErrorDetail("EMAIL_NOT_VERIFIED", "email", "Please verify your email.");
            return ApiResponse.fail("Login failed", 403, List.of(error));
        }

        String token = jwtService.generateToken(player.getEmail(),player.getId());
        LoginResponse loginResponse = new LoginResponse(token, player.getUsername());
        return ApiResponse.ok("Login successful", loginResponse);
    }

    public ApiResponse<Void> forgotPassword(ForgotPasswordRequest request) {
        Optional<Player> optional = playerRepository.findByEmail(request.email());
        if (optional.isEmpty()) {
            return ApiResponse.ok("If email exists, reset instructions were sent.", null); // safe response
        }

        Player player = optional.get();
        player.setPasswordResetToken(UUID.randomUUID().toString());
        playerRepository.save(player);
        sendResetEmail(player); // simulate

        return ApiResponse.ok("If email exists, reset instructions were sent.", null);
    }

    public ApiResponse<Void> resetPassword(ResetPasswordRequest request) {
        Optional<Player> optional = playerRepository.findAll().stream()
                .filter(p -> request.token().equals(p.getPasswordResetToken()))
                .findFirst();

        if (optional.isEmpty()) {
            var error = new ErrorDetail("INVALID_TOKEN", "token", "Invalid or expired token.");
            return ApiResponse.fail("Reset failed", 400, List.of(error));
        }

        Player player = optional.get();
        player.setPassword(passwordEncoder.encode(request.newPassword()));
        player.setPasswordResetToken(null);
        playerRepository.save(player);

        return ApiResponse.ok("Password updated successfully.", null);
    }

    public ApiResponse<Void> verifyEmail(String token) {
        Optional<Player> optional = playerRepository.findAll().stream()
                .filter(p -> token.equals(p.getEmailVerificationToken()))
                .findFirst();

        if (optional.isEmpty()) {
            var error = new ErrorDetail("INVALID_TOKEN", "token", "Invalid or expired token.");
            return ApiResponse.fail("Verification failed", 400, List.of(error));
        }

        Player player = optional.get();
        player.setEmailVerified(true);
        player.setEmailVerificationToken(null);
        playerRepository.save(player);

        return ApiResponse.ok("Email verified successfully!", null);
    }

    private void sendVerificationEmail(Player player) {
        System.out.println("📧 [SIMULATION] Verification email sent to " + player.getEmail() +
                " with token: " + player.getEmailVerificationToken());
    }

    private void sendResetEmail(Player player) {
        System.out.println("📧 [SIMULATION] Password reset email sent to " + player.getEmail() +
                " with token: " + player.getPasswordResetToken());
    }

    private Coordinates findAvailableCoordinates(Long playerId) {
        Coordinates coords = mapService.allocateSpawnTile(playerId);

        return coords;
    }

    @Transactional
    public void recordHeartbeat(Long playerId) {
        playerRepository.updateLastActiveById(playerId, LocalDateTime.now());
    }

    public List<OnlinePlayerDto> getOnlinePlayers() {
        return playerRepository.findOnlinePlayerSummaries(LocalDateTime.now().minusMinutes(5));
    }

}
