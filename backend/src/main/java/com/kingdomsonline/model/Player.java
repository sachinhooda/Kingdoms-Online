package com.kingdomsonline.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Player extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private Kingdom kingdom;

    private boolean isActive = true;
    private boolean isBanned = false;

    @Column(name = "email_verified")
    private boolean emailVerified = false;

    @Column(name = "email_verification_token")
    private String emailVerificationToken;

    @Column(name = "password_reset_token")
    private String passwordResetToken;

    @Column(name = "last_active_at")
    private LocalDateTime lastActiveAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alliance_id")
    private Alliance alliance;

    // Getters and setters
}
