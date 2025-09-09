package com.kakaotechcampus.team16be.auth.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum KakaoErrorCode {

    // 500 INTERNAL_SERVER_ERROR
    TOKEN_REQUEST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 토큰 요청에 실패했습니다."),
    USER_INFO_REQUEST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 사용자 정보 요청에 실패했습니다."),
    CONNECTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 서버와의 연결에 실패했습니다."),

    // 503 SERVICE_UNAVAILABLE
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "카카오 서비스가 일시적으로 불가능합니다. 잠시 후 다시 시도해주세요.");

    private final HttpStatus httpStatus; // HTTP 상태 코드
    private final String message; // 에러 메시지

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
