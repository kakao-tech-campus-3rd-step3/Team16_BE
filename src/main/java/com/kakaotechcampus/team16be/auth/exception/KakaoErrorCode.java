package com.kakaotechcampus.team16be.auth.exception;

import com.kakaotechcampus.team16be.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum KakaoErrorCode implements ErrorCode {

    // 500 INTERNAL_SERVER_ERROR
    TOKEN_REQUEST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"AUTH-003", "카카오 토큰 요청에 실패했습니다."),
    USER_INFO_REQUEST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"AUTH-004", "카카오 사용자 정보 요청에 실패했습니다."),
    CONNECTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH-005", "카카오 서버와의 연결에 실패했습니다.");

    private final HttpStatus status; // HTTP 상태 코드
    private final String code;
    private final String message; // 에러 메시지
}
