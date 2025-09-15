package com.kakaotechcampus.team16be.groundrule.exception;

import lombok.Getter;

@Getter
public class GroundRuleException extends RuntimeException {

  private final GroundRuleErrorCode errorCode;

  public GroundRuleException(GroundRuleErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
