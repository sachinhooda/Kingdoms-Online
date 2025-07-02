package com.kingdomsonline.dto;

import com.kingdomsonline.config.TechRequirement;

import java.time.LocalDateTime;

public record TechnologyViewDto(
    String id,
    String name,
    String description,
    int currentLevel,
    int maxLevel,
    UpgradeInfo upgrade,
    String nextLevelEffect,
    TechRequirement requirements
) {
    public record UpgradeInfo(
        LocalDateTime startedAt,
        LocalDateTime finishedAt
    ) {}
}
