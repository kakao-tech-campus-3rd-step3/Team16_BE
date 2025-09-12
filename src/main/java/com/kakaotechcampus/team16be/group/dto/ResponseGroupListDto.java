package com.kakaotechcampus.team16be.group.dto;

import com.kakaotechcampus.team16be.group.domain.Group;

public record ResponseGroupListDto(
        Long groupId,
        String name,
        String intro,
        String safetyTag,
        String coverImageUrl
) {

    public static ResponseGroupListDto from(Group group, String coverImageUrl) {
        return new ResponseGroupListDto(
                group.getId(),
                group.getName(),
                group.getIntro(),
                group.getSafetyTag().name(),
                coverImageUrl);
    }

}
