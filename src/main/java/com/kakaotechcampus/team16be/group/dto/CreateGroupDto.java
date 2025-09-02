package com.kakaotechcampus.team16be.group.dto;

import com.kakaotechcampus.team16be.group.domain.Group;


public record CreateGroupDto(String name, String intro, Integer capacity) {
    public Group dtoConvertEntity() {
        return new Group(name, intro, capacity);
    }
}