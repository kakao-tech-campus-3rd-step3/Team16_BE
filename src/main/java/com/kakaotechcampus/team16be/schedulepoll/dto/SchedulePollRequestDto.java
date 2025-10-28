package com.kakaotechcampus.team16be.schedulepoll.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record SchedulePollRequestDto(
    String title,
    String description,
    List<LocalDate> dates,
    List<TimeSlotDto> timeSlots
) {
  public record TimeSlotDto(
      LocalTime startTime,
      LocalTime endTime
  ){}
}
