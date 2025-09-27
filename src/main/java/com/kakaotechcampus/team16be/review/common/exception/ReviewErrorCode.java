package com.kakaotechcampus.team16be.review.common.exception;

import com.kakaotechcampus.team16be.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements ErrorCode {

    ACTIVE_CANNOT_REVIEW(HttpStatus.FORBIDDEN, "REVIEW-001", "탈퇴한 회원에 대해서만 리뷰가 가능합니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
