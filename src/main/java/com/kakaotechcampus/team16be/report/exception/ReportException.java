package com.kakaotechcampus.team16be.report.exception;

import com.kakaotechcampus.team16be.common.exception.BaseException;
import lombok.Getter;

@Getter
public class ReportException extends BaseException {

  private final ReportErrorCode errorCode;

  public ReportException(ReportErrorCode errorCode) {
    this.errorCode = errorCode;
  }

  @Override
  public String getMessage() {
    return errorCode.getMessage();
  }

  @Override
  public String getCode() {
    return errorCode.getCode();
  }

  @Override
  public int getStatus() {
    return errorCode.getStatus().value();
  }
}
