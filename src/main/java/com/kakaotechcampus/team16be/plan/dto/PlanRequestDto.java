package com.kakaotechcampus.team16be.plan.dto;

import java.time.LocalDateTime;

public record PlanRequestDto(
    String title,
    String description,
    Integer capacity,
    LocalDateTime startTime,
    LocalDateTime endTime,
    LocationDto location
) {

  public record LocationDto(
      String name,
      Double latitude,
      Double longitude
  ) {}
}