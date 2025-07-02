package com.kingdomsonline.model;

import java.time.LocalDateTime;

public record TechnologyState(
    int currentLevel,
    UpgradeInfo upgrade
) {
    public record UpgradeInfo(LocalDateTime startedAt, LocalDateTime finishedAt) {}
}
