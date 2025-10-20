package com.kakaotechcampus.team16be.schedulepoll.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record TimeSlotListResponseDto(
    Long pollId,
    String title,
    Boolean isClosed,
    List<TimeSlotWithVotes> timeSlots
) {
  public record TimeSlotWithVotes(
    Long slotId,
    LocalDate date,
    LocalTime startTime,
    LocalTime endTime,
    Integer availableCount,
    List<Long> availableUserIds
  ){}
}
