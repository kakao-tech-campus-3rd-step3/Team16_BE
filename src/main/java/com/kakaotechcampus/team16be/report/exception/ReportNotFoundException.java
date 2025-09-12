package com.kakaotechcampus.team16be.report.exception;

import lombok.Getter;

@Getter
public class ReportNotFoundException extends RuntimeException {

  private final ErrorCode errorCode;

  public ReportNotFoundException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
