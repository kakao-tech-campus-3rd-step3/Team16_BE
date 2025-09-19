package com.kakaotechcampus.team16be.plan.dto;

import java.time.LocalDateTime;

public record PlanResponseDto(
    Long id,
    String title,
    String description,
    Integer capacity,
    LocalDateTime startTime,
    LocalDateTime endTime,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
