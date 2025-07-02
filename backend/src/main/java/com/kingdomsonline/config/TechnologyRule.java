package com.kingdomsonline.config;

import java.util.List;

public record TechnologyRule(
    String id,
    String name,
    String description,
    int maxLevel,
    List<TechRequirement> requirements
) {}
