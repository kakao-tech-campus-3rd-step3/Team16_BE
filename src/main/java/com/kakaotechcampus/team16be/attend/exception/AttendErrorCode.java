package com.kakaotechcampus.team16be.attend.exception;

import com.kakaotechcampus.team16be.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AttendErrorCode implements ErrorCode {
    // 404 Not Found
    ATTEND_NOT_FOUND(HttpStatus.NOT_FOUND, "ATTEND-001", "해당 출석 정보를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
