package com.kakaotechcampus.team16be.auth.dto;

import com.kakaotechcampus.team16be.user.domain.VerificationStatus;

public record StudentVerificationStatusResponse(
        boolean valid,
        VerificationStatus verificationStatus,
        String reason
) {
    public static StudentVerificationStatusResponse of(
            boolean valid,
            VerificationStatus status,
            String reason
    ) {
        return new StudentVerificationStatusResponse(valid, status, reason);
    }
}
