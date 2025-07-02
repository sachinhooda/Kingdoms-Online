package com.kingdomsonline.config;

import java.util.List;

public record TechRequirement(
    int level,
    int grain,
    int gold,
    int wood,
    int stone,
    List<String> requires,
    String effect // <-- Add this
) {}
