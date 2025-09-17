package com.kakaotechcampus.team16be.report.exception;

import lombok.Getter;

@Getter
public class ReportException extends RuntimeException {

  private final ReportErrorCode reportErrorCode;

  public ReportException(ReportErrorCode reportErrorCode) {
    super(reportErrorCode.getMessage());
    this.reportErrorCode = reportErrorCode;
  }
}
