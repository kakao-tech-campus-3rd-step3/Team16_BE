package com.kakaotechcampus.team16be.schedulepoll.dto;

import java.util.List;

public record SchedulePollListResponseDto(
    List<SchedulePollSummaryDto> polls
) {
  public record SchedulePollSummaryDto(
      Long pollId,
      String title,
      String creatorName,
      Boolean isClosed,
      String createdAt
  ){}
}
