package com.kakaotechcampus.team16be.groupMember.dto;

import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;

import java.util.List;

public record SignResponseDto(
        Long userId,
        String intro
) {
    public static List<SignResponseDto> from(List<GroupMember> members) {
        return members.stream()
                .map(m -> new SignResponseDto(
                        m.getUser().getId(),
                        m.getIntro()
                ))
                .toList();
    }
}
