package com.kakaotechcampus.team16be.report.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReportErrorCode {

  REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "REPORT-002", "해당 신고가 존재하지 않습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;
}
