package com.kakaotechcampus.team16be.group.dto;

import com.kakaotechcampus.team16be.group.domain.Group;

import java.time.LocalDateTime;

public record ResponseSingleGroupDto(
        Long groupId,
        String name,
        String intro,
        String safetyTag,
        String coverImageUrl,
        LocalDateTime createdAt,
        int capacity,
        Double score
) {

    public static ResponseSingleGroupDto from(Group group, String coverImageUrl) {
        return new ResponseSingleGroupDto(
                group.getId(),
                group.getName(),
                group.getIntro(),
                group.getSafetyTag().name(),
                coverImageUrl,
                group.getCreatedAt(),
                group.getCapacity(),
                group.getScore()
        );
    }
}
