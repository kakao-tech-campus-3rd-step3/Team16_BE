package com.kakaotechcampus.team16be.user.dto;

import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.domain.VerificationStatus;

import java.util.List;
import java.util.Map;

public record UserInfoResponse(
        String id,
        String nickname,
        String profileImageUrl,
        VerificationStatus isStudentVerified,
        Map<String, List<String>> groups
) {
    public static UserInfoResponse of(User user, Map<String, List<String>> groups) {
        return new UserInfoResponse(
                user.getId().toString(),
                user.getNickname(),
                user.getProfileImageUrl(),
                user.getVerificationStatus(),
                groups
        );
    }
}
