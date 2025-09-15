package com.kakaotechcampus.team16be.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum JwtErrorCode {
    // 401 Unauthorized
    WRONG_HEADER_TOKEN(HttpStatus.UNAUTHORIZED,"AUTH-001", "잘못된 토큰입니다."),

    // 403 Forbidden
    NOT_ADMIN(HttpStatus.FORBIDDEN,"AUTH-002", "관리자 권한이 없습니다." );

    private final HttpStatus status; // HTTP 상태 코드
    private final String code;
    private final String message; // 에러 메시지
}
