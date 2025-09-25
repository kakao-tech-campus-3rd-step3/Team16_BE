package com.kakaotechcampus.team16be.group.exception;

import com.kakaotechcampus.team16be.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GroupErrorCode implements ErrorCode {

    GROUP_NAME_DUPLICATE(HttpStatus.CONFLICT, "GROUP-001", "해당 모임의 이름은 이미 존재합니다."),
    WRONG_GROUP_NAME(HttpStatus.BAD_REQUEST, "GROUP-002", "올바르지 않은 모임 이름입니다."),
    WRONG_GROUP_CAPACITY(HttpStatus.BAD_REQUEST, "GROUP-003", "모임 최소 인원 수는 1명입니다."),
    WRONG_GROUP_LEADER(HttpStatus.BAD_REQUEST, "GROUP-008", "해당 유저는 그룹장이 아닙니다"),
    GROUP_NO_INPUT(HttpStatus.BAD_REQUEST, "GROUP-004", "입력 값이 존재하지 않습니다."),
    GROUP_CANNOT_FOUND(HttpStatus.NOT_FOUND, "GROUP-005", "모임이 존재하지 않습니다."),
    GROUP_NO_AUTHORITY(HttpStatus.FORBIDDEN, "GROUP-006", "해당 권한이 없습니다."),
    GROUP_NO_FILENAME(HttpStatus.BAD_REQUEST, "GROUP-007", "해당 파일이 없습니다."),
    WRONG_GROUP_ACCESS(HttpStatus.FORBIDDEN, "GROUP-009", "잘못된 그룹 접근입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}