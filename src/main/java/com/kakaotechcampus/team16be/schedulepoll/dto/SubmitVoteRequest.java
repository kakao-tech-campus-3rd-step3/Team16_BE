package com.kakaotechcampus.team16be.schedulepoll.dto;

import java.util.List;

public record SubmitVoteRequest(
    List<Long> availableSlotIds
) {
}
