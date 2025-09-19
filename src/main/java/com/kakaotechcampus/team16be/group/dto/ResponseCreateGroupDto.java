package com.kakaotechcampus.team16be.group.dto;

import com.kakaotechcampus.team16be.group.domain.Group;

public record ResponseCreateGroupDto(Long groupId) {

    public static ResponseCreateGroupDto from(Group group) {
        return new ResponseCreateGroupDto(group.getId());
    }
}
