package com.kakaotechcampus.team16be.groundrule.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GroundRuleErrorCode {

  RULE_NOT_FOUND(HttpStatus.NOT_FOUND, "RULE-001", "해당 룰이 존재하지 않습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;
}
