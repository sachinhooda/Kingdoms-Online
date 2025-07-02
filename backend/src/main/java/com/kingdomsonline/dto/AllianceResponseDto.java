package com.kingdomsonline.dto;

public record AllianceResponseDto(
    Long id,
    String name,
    String tag,
    String description,
    String founder,
    String leader
) {}
