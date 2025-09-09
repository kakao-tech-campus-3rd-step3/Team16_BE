package com.kakaotechcampus.team16be.user.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum UserErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    PROFILE_IMAGE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 프로필 이미지가 존재합니다"),
    PROFILE_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "프로필 이미지를 찾을 수 없습니다.");

    private final HttpStatus httpStatus; // HTTP 상태 코드
    private final String message; // 에러 메시지

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
