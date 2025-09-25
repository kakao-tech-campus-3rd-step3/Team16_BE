package com.kakaotechcampus.team16be.plan.domain;

import com.kakaotechcampus.team16be.common.BaseEntity;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.plan.dto.PlanRequestDto;
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

  @Column(name = "start_time", nullable = false)
  private LocalDateTime startTime;

  @Column(name = "end_time", nullable = false)
  private LocalDateTime endTime;

  @Builder
<<<<<<< Updated upstream
  public Plan(Group group, String title, String description, Integer capacity, LocalDateTime startTime, LocalDateTime endTime){
    if(capacity <= 0){
      throw new IllegalArgumentException("참여 인원은 1명 이상이어야 합니다.");
    }
    if (startTime.isAfter(endTime)) {
      throw new IllegalArgumentException("시작 시간이 종료 시간 이후일 수 없습니다.");
    }
=======
  public Plan(Group group, String title, String description, Integer capacity, LocalDateTime startTime, LocalDateTime endTime) {
    validateCapacity(capacity);
    validateTimeRange(startTime, endTime);
>>>>>>> Stashed changes

    this.group = group;
    this.title = title;
    this.description = description;
    this.capacity = capacity;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public void changePlan(PlanRequestDto dto) {
    if (dto.title() != null)
      this.title = dto.title();

    if (dto.description() != null)
      this.description = dto.description();

    if (dto.capacity() != null){
      if(dto.capacity() <= 0) throw new IllegalArgumentException("참가 인원 수는 1명 이상이어야 합니다.");
    }

<<<<<<< Updated upstream
    if (dto.startTime() != null)
      this.startTime = dto.startTime();

    if (dto.endTime() != null)
      this.endTime = dto.endTime();
=======
    this.startTime = newStartTime;
    this.endTime = newEndTime;
  }

  public static Plan create(Group group, String title, String description,
      Integer capacity, LocalDateTime startTime, LocalDateTime endTime
  ){
    return Plan.builder()
               .group(group)
               .title(title)
               .description(description)
               .capacity(capacity)
               .startTime(startTime)
               .endTime(endTime)
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
>>>>>>> Stashed changes
  }
}
