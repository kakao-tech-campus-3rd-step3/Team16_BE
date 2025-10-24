package com.kakaotechcampus.team16be.schedulepoll.dto;

import java.util.List;

public record MyVoteResponseDto(
    Long pollId,
    List<Long> votedSlotIds
) {
}
