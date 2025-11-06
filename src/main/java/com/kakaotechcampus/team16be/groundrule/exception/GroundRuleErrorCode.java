package com.kakaotechcampus.team16be.groundrule.exception;

import com.kakaotechcampus.team16be.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GroundRuleErrorCode implements ErrorCode {

    RULE_NOT_FOUND(HttpStatus.NOT_FOUND, "RULE-001", "해당 룰이 존재하지 않습니다."),
    RULE_NOT_ADD(HttpStatus.BAD_REQUEST, "RULE-002", "모임 당 룰의 최대 개수는 5개입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
