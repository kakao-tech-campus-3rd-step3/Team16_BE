package com.kakaotechcampus.team16be.auth.exception;

import org.springframework.http.HttpStatus;

public enum JwtErrorCode {
    // 401 Unauthorized
    WRONG_HEADER_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다.");

    private final HttpStatus httpStatus; // HTTP 상태 코드
    private final String message; // 에러 메시지

    JwtErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
