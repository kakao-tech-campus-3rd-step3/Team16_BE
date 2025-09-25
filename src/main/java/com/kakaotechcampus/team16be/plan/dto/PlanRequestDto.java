package com.kakaotechcampus.team16be.plan.dto;

import java.time.LocalDateTime;

public record PlanRequestDto(
    String title,
    String description,
    Integer capacity,
    Integer attendee,
    LocalDateTime startTime,
    LocalDateTime endTime,
    Double latitude,
    Double longitude
) {
}