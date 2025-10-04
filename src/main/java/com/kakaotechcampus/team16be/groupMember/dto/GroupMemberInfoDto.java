package com.kakaotechcampus.team16be.groupMember.dto;

import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.groupMember.domain.GroupRole;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.domain.VerificationStatus;

import java.util.List;
import java.util.stream.Collectors;

public record GroupMemberInfoDto(
        Long userId,
        VerificationStatus verificationStatus,
        List<GroupRoleDto> groups
) {
    public static GroupMemberInfoDto from(User user, List<GroupMember> members) {
        List<GroupRoleDto> groupRoleDtos = members.stream()
                .map(GroupRoleDto::from)
                .collect(Collectors.toList());

        return new GroupMemberInfoDto(user.getId(), user.getVerificationStatus(), groupRoleDtos);
    }

    public record GroupRoleDto(
            Long groupId,
            String groupName,
            GroupRole role
    ) {
        public static GroupRoleDto from(GroupMember member) {
            return new GroupRoleDto(
                    member.getGroup().getId(),
                    member.getGroup().getName(),
                    member.getRole()
            );
        }
    }
}