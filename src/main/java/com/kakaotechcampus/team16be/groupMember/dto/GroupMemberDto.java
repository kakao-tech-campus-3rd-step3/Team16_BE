package com.kakaotechcampus.team16be.groupMember.dto;

import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.groupMember.domain.GroupRole;

import java.util.List;

public record GroupMemberDto(
        Long id,
        String groupName,
        Long userId,
        String nickname,
        GroupRole groupRole,
        String profileImageUrl
) {

    public static List<GroupMemberDto> from(List<GroupMember> members) {
        return members.stream()
                .map(member -> new GroupMemberDto(
                        member.getId(),
                        member.getGroup().getName(),
                        member.getUser().getId(),
                        member.getUser().getNickname(),
                        member.getRole(),
                        member.getUser().getProfileImageUrl()
                ))
                .toList();
    }
}