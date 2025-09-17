package com.kakaotechcampus.team16be.plan.dto;

import java.time.LocalDateTime;

public record PlanRequestDto(
    Long groupId,
    String title,
    String description,
    int capacity,
    LocalDateTime startTime,
    LocalDateTime endTime
) {

}