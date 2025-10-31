package com.kakaotechcampus.team16be.plan.dto;

import com.kakaotechcampus.team16be.plan.domain.Location;
import com.kakaotechcampus.team16be.plan.domain.Plan;
import java.time.LocalDateTime;

public record PlanResponseDto(
    Long id,
    String title,
    String description,
    Integer capacity,
    LocalDateTime startTime,
    LocalDateTime endTime,
    String coverImageUrl,
    LocationDto location
) {

  public record LocationDto(
      String name,
      Double latitude,
      Double longitude
  ) {
    public static LocationDto from(Location location) {
      if (location == null) {
        return null;
      }
      return new LocationDto(location.getName(), location.getLatitude(), location.getLongitude());
    }
  }

  public static PlanResponseDto from(Plan plan,String fullUrl) {
    return new PlanResponseDto(
            plan.getId(),
            plan.getTitle(),
            plan.getDescription(),
            plan.getCapacity(),
            plan.getStartTime(),
            plan.getEndTime(),
            fullUrl,
            LocationDto.from(plan.getLocation())
    );
  }
}
