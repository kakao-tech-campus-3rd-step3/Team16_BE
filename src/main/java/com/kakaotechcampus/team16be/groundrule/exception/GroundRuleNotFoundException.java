package com.kakaotechcampus.team16be.groundrule.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class GroundRuleNotFoundException extends RuntimeException {

  private final ErrorCode errorCode;

  public GroundRuleNotFoundException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
