package com.kakaotechcampus.team16be.schedulepoll.dto;

import com.kakaotechcampus.team16be.schedulepoll.domain.SchedulePoll;
import java.time.LocalDateTime;

public record SchedulePollResponseDto(
    Long pollId,
    String title,
    String description,
    Long creatorId,
    String creatorName,
    Boolean isClosed,
    LocalDateTime createdAt
) {
  public static SchedulePollResponseDto from(SchedulePoll poll){
    return new SchedulePollResponseDto(
        poll.getId(),
        poll.getTitle(),
        poll.getDescription(),
        poll.getCreator().getId(),
        poll.getCreator().getNickname(),
        poll.getIsClosed(),
        poll.getCreatedAt()
    );
  }
}
