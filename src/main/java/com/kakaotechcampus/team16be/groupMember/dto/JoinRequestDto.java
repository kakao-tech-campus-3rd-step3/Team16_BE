package com.kakaotechcampus.team16be.groupMember.dto;

import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;

import java.util.List;

public record JoinRequestDto(
        Long userId,
        String intro
) {
    public static List<JoinRequestDto> from(List<GroupMember> members) {
        return members.stream()
                .map(m -> new JoinRequestDto(
                        m.getUser().getId(),
                        m.getIntro()
                ))
                .toList();
    }
}
