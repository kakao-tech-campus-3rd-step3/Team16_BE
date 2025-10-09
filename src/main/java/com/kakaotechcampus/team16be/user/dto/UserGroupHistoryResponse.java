package com.kakaotechcampus.team16be.user.dto;

import com.kakaotechcampus.team16be.group.domain.SafetyTag;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMemberStatus;

import java.time.format.DateTimeFormatter;

public record UserGroupHistoryResponse(
        Long groupId,
        String name,
        GroupMemberStatus groupMemberStatus,
        SafetyTag safetyTag,
        String joinAt,
        String leftAt
) {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy. MM. dd");

    public static UserGroupHistoryResponse from(GroupMember member) {
        String joinAt = member.getCreatedAt() != null ? member.getCreatedAt().format(FORMATTER) : null;
        String leftAt = member.getLeftAt() != null ? member.getLeftAt().format(FORMATTER) : null;

        return new UserGroupHistoryResponse(
                member.getGroup().getId(),
                member.getGroup().getName(),
                member.getStatus(),
                member.getGroup().getSafetyTag(),
                joinAt,
                leftAt
        );
    }
}
