package com.kingdomsonline.dto;

public record KingdomDetailsResponse(
    String playerName,
    String castleName,
    int x,
    int y,
    int gold,
    int wood,
    int grain,
    int stone
) {}
