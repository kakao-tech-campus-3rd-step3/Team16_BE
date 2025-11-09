package com.kakaotechcampus.team16be.user.dto;

import com.kakaotechcampus.team16be.group.dto.GroupMembership;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.domain.VerificationStatus;

public record UserInfoResponse(
        String id,
        String nickname,
        String profileImageUrl,
        Double userScore,
        VerificationStatus studentVerifiedStatus,
        GroupMembership groups
) {
    public static UserInfoResponse of(User user, GroupMembership groups, String profileImageUrl) {
        return new UserInfoResponse(
                user.getId().toString(),
                user.getNickname(),
                profileImageUrl,
                user.getScore(),
                user.getVerificationStatus(),
                groups
        );
    }
}
