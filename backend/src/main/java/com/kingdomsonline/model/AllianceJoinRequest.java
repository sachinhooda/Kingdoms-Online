package com.kingdomsonline.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "alliance_join_request", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"player_id", "alliance_id"})
})
@Getter
@Setter
public class AllianceJoinRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Player player;

    @ManyToOne(optional = false)
    private Alliance alliance;

    @Column(nullable = false)
    private LocalDateTime requestedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        PENDING,
        APPROVED,
        REJECTED
    }
}
