package com.kakaotechcampus.team16be.plan.domain;

import com.kakaotechcampus.team16be.common.BaseEntity;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.plan.dto.PlanRequestDto;
import com.kakaotechcampus.team16be.plan.exception.PlanErrorCode;
import com.kakaotechcampus.team16be.plan.exception.PlanException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

  @Column(name = "start_time", nullable = false)
  private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

  @Builder
  public Plan(Group group, String title, String description, Integer capacity, LocalDateTime startTime, LocalDateTime endTime, Location location){
    validateCapacity(capacity);
    validateTimeRange(startTime, endTime);

    this.group = group;
    this.title = title;
    this.description = description;
    this.capacity = capacity;
    this.startTime = startTime;
    this.endTime = endTime;
    this.location = location;
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
      this.capacity = dto.capacity();
    }

    this.startTime = newStartTime;
    this.endTime = newEndTime;

    if (dto.location() != null) {
      this.location = Location.builder()
                              .name(dto.location().name())
                              .latitude(dto.location().latitude())
                              .longitude(dto.location().longitude())
                              .build();
    }
  }

  public static Plan create(Group group, String title, String description,
      Integer capacity, LocalDateTime startTime, LocalDateTime endTime, Location location
      ) {
    return Plan.builder()
               .group(group)
               .title(title)
               .description(description)
               .capacity(capacity)
               .startTime(startTime)
               .endTime(endTime)
               .location(location)
               .build();
  }

  private void validateCapacity(Integer capacity) {
    if(capacity == null || capacity <= 0) {
      throw new PlanException(PlanErrorCode.INVALID_CAPACITY);
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
}