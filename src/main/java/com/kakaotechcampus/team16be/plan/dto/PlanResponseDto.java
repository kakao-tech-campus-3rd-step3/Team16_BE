package com.kakaotechcampus.team16be.plan.dto;

import com.kakaotechcampus.team16be.plan.domain.Location;
import com.kakaotechcampus.team16be.plan.domain.Plan;
import java.time.LocalDateTime;

public record PlanResponseDto(
        Long id,
        String title,
        String description,
        Integer capacity,
        Long headCount,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String coverImageUrl,
        LocationDto location
) {

    public static PlanResponseDto from(Plan plan, String fullUrl,Long headCount) {
        return new PlanResponseDto(
                plan.getId(),
                plan.getTitle(),
                plan.getDescription(),
                plan.getCapacity(),
                headCount,
                plan.getStartTime(),
                plan.getEndTime(),
                fullUrl,
                LocationDto.from(plan.getLocation())
        );
    }

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
}
