package com.kakaotechcampus.team16be.plan.domain;

import com.kakaotechcampus.team16be.common.BaseEntity;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.plan.dto.PlanRequestDto;
import com.kakaotechcampus.team16be.plan.exception.PlanErrorCode;
import com.kakaotechcampus.team16be.plan.exception.PlanException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Plan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "attendee", nullable = false)
    private Integer attendee;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Builder
    public Plan(Group group, String title, String description, Integer capacity, Integer attendee,
                LocalDateTime startTime, LocalDateTime endTime, Double latitude, Double longitude) {
        validateCapacity(capacity);
        validateAttendee(attendee);
        validateTimeRange(startTime, endTime);
        validateCoordinates(latitude, longitude);

        this.group = group;
        this.title = title;
        this.description = description;
        this.capacity = capacity;
        this.attendee = attendee != null ? attendee : 0;
        this.startTime = startTime;
        this.endTime = endTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void changePlan(PlanRequestDto dto) {
        LocalDateTime newStartTime = dto.startTime() != null ? dto.startTime() : this.startTime;
        LocalDateTime newEndTime = dto.endTime() != null ? dto.endTime() : this.endTime;
        validateTimeRange(newStartTime, newEndTime);

        if(dto.title() != null) {
            this.title = dto.title();
        }

        if(dto.description() != null) {
            this.description = dto.description();
        }

        if(dto.capacity() != null) {
            validateCapacity(dto.capacity());
            validateAttendeeCapacityRelation(dto.capacity(), this.attendee);
            this.capacity = dto.capacity();
        }

        this.startTime = newStartTime;
        this.endTime = newEndTime;

        if(dto.latitude() != null && dto.longitude() != null) {
            validateCoordinates(dto.latitude(), dto.longitude());
            this.latitude = dto.latitude();
            this.longitude = dto.longitude();
        }
    }

    // =========== 출석체크랑 연계되는 메소드 ============
    public void increaseAttendee() {
        if(this.attendee >= this.capacity) {
            throw new PlanException(PlanErrorCode.PLAN_FULL);
        }
        this.attendee++;
    }

    public void decreaseAttendee() {
        if(this.attendee <= 0) {
            throw new PlanException(PlanErrorCode.NO_ATTENDEE_TO_REMOVE);
        }
        this.attendee--;
    }
    // =========== 출석체크랑 연계되는 메소드 ============


    public static Plan create(Group group, String title, String description,
                              Integer capacity, LocalDateTime startTime, LocalDateTime endTime,
                              Double latitude, Double longitude) {
        return Plan.builder()
                .group(group)
                .title(title)
                .description(description)
                .capacity(capacity)
                .attendee(0)
                .startTime(startTime)
                .endTime(endTime)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

    private void validateCapacity(Integer capacity) {
        if(capacity == null || capacity <= 0) {
            throw new PlanException(PlanErrorCode.INVALID_CAPACITY);
        }
    }

    private void validateAttendee(Integer attendee) {
        if(attendee != null && attendee < 0) {
            throw new PlanException(PlanErrorCode.INVALID_ATTENDEE_COUNT);
        }
    }

    private void validateAttendeeCapacityRelation(Integer capacity, Integer attendee) {
        if(attendee > capacity) {
            throw new PlanException(PlanErrorCode.ATTENDEE_EXCEEDS_CAPACITY);
        }
    }

    private void validateTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            throw new PlanException(PlanErrorCode.TIME_REQUIRED);
        }

        if (!startTime.isBefore(endTime)) {
            throw new PlanException(PlanErrorCode.INVALID_TIME_RANGE);
        }
    }

    private void validateCoordinates(Double latitude, Double longitude) {
        if (latitude == null || longitude == null) {
            throw new PlanException(PlanErrorCode.COORDINATES_REQUIRED);
        }

        if (latitude < -90.0 || latitude > 90.0 || longitude < -180.0 || longitude > 180.0) {
            throw new PlanException(PlanErrorCode.INVALID_COORDINATE);
        }
    }
}
