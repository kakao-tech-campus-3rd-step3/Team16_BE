package com.kakaotechcampus.team16be.schedulepoll.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PollTimeSlot {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "schedule_poll_id", nullable = false)
  private SchedulePoll schedulePoll;

  @Column(name = "poll_date", nullable = false)
  private LocalDate pollDate;

  @Column(name = "start_time", nullable = false)
  private LocalTime startTime;

  @Column(name = "end_time", nullable = false)
  private LocalTime endTime;

  @Builder
  public PollTimeSlot(SchedulePoll schedulePoll, LocalDate pollDate, LocalTime startTime, LocalTime endTime){
    this.schedulePoll = schedulePoll;
    this.pollDate = pollDate;
    this.startTime = startTime;
    this.endTime = endTime;
  }
}
