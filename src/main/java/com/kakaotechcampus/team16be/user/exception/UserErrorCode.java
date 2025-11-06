package com.kakaotechcampus.team16be.user.exception;

import com.kakaotechcampus.team16be.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-005", "유저를 찾을 수 없습니다."),
    PROFILE_IMAGE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "USER-006", "이미 프로필 이미지가 존재합니다"),
    PROFILE_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-007", "프로필 이미지를 찾을 수 없습니다."),
    INVALID_SCORE(HttpStatus.BAD_REQUEST, "USER-008", "점수는 0 이상이어야 합니다."),
    CANNOT_WITHDRAW_LEADER(HttpStatus.BAD_REQUEST, "USER-009", "그룹의 리더는 탈퇴할 수 없습니다.");

    private final HttpStatus status; // HTTP 상태 코드
    private final String code; //사용자 정의 에러 코드
    private final String message; // 에러 메시지
}
