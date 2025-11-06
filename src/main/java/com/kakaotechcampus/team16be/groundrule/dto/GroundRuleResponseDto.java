package com.kakaotechcampus.team16be.groundrule.dto;

import java.time.LocalDateTime;

public record GroundRuleResponseDto(
        Long id,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
