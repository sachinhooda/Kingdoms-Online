package com.kingdomsonline.dto;

import java.time.LocalDateTime;

public record JoinRequestResponseDto(
    Long requestId,
    Long playerId,
    String username,
    LocalDateTime requestedAt
) {}
