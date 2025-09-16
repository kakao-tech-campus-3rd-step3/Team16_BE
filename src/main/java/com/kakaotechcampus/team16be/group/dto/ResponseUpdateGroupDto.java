package com.kakaotechcampus.team16be.group.dto;

import com.kakaotechcampus.team16be.group.domain.Group;

public record ResponseUpdateGroupDto(Long groupId) {

    public static ResponseUpdateGroupDto from(Group group) {
        return new ResponseUpdateGroupDto(group.getId());
    }
}
