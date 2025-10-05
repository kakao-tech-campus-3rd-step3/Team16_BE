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
    CONNECTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH-005", "카카오 서버와의 연결에 실패했습니다."),
    TOKEN_REQUEST_FAILED_HTTP_STATUS(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH-006", "카카오 토큰 요청이 2xx 상태 코드가 아닙니다."),
    TOKEN_REQUEST_FAILED_NULL_BODY(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH-007", "카카오 토큰 API 응답 body가 null입니다."),
    TOKEN_REQUEST_FAILED_NO_TOKEN(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH-008", "카카오 토큰 API 응답에 access_token이 없습니다."),
    TOKEN_REQUEST_FAILED_CLIENT(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH-009", "카카오 토큰 요청 중 클라이언트 에러가 발생했습니다."),
    TOKEN_REQUEST_FAILED_SERVER(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH-010", "카카오 토큰 요청 중 서버 에러가 발생했습니다."),
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH-011", "카카오 토큰 요청 중 알 수 없는 에러가 발생했습니다.");

    private final HttpStatus status; // HTTP 상태 코드
    private final String code;
    private final String message; // 에러 메시지
}
