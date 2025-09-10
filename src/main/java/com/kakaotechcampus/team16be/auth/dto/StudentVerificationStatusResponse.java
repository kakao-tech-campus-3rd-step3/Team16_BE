package com.kakaotechcampus.team16be.auth.dto;

import com.kakaotechcampus.team16be.user.domain.VerificationStatus;

public record StudentVerificationStatusResponse (
        boolean valid,//유저가 DB에 존재하는지 여부
        VerificationStatus verificationStatus,//인증 상태 ("PENDING", "APPROVED", "REJECTED")
        String reason//거절 사유
){
    public static StudentVerificationStatusResponse of(
            boolean valid,
            VerificationStatus status,
            String reason
    ) {
        return new StudentVerificationStatusResponse(valid, status, reason);
    }
}
