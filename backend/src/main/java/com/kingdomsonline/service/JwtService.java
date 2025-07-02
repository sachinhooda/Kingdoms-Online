package com.kingdomsonline.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles JWT token generation and parsing logic.
 */
@Service
public class JwtService {

    @Value("${security.jwt.secret}")
    private String secretKey;

    private static final long EXPIRATION = 1000 * 60 * 60 * 10; // 10 hours

    public String generateToken(String email, Long playerId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("playerId", playerId);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(24, ChronoUnit.HOURS)))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


    public String getUsername(String token) {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long extractPlayerIdFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Claims claims = extractAllClaims(token);
        return claims.get("playerId", Long.class);
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractEmailFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return extractEmail(token);
    }
}
