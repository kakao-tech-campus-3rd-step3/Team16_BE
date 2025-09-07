package com.kakaotechcampus.team16be.auth.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum JwtErrorCode {
    // 401 Unauthorized
    WRONG_HEADER_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다.");

    private final HttpStatus httpStatus; // HTTP 상태 코드
    private final String message; // 에러 메시지

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
