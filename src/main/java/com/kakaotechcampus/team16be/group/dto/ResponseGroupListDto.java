package com.kakaotechcampus.team16be.group.dto;

import com.kakaotechcampus.team16be.group.domain.Group;

import java.util.ArrayList;
import java.util.List;

public record ResponseGroupListDto(
        Long groupId,
        String name,
        String intro,
        String safetyTag,
        String coverImageUrl
) {

    public static ResponseGroupListDto from(Group group) {
        return new ResponseGroupListDto(
                group.getId(),
                group.getName(),
                group.getIntro(),
                group.getSafetyTag().name(),
                group.getCoverImageUrl()
        );
    }

    public static List<ResponseGroupListDto> from(List<Group> groups) {
        List<ResponseGroupListDto> result = new ArrayList<>();
        for (Group group : groups) {
            result.add(from(group));
        }
        return result;
    }
}
