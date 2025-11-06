package com.kakaotechcampus.team16be.admin.dto;

public record AdminUserVerificationView(
        Long userId,
        String nickname,
        String imageUrl,
        String verificationStatus
) {
}
