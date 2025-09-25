package com.kakaotechcampus.team16be.plan.dto;

import java.time.LocalDateTime;

public record PlanResponseDto(
    Long id,
    String title,
    String description,
    Integer capacity,
    Integer attendee,
    LocalDateTime startTime,
    LocalDateTime endTime,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Double latitude,
    Double longitude
) {
}
