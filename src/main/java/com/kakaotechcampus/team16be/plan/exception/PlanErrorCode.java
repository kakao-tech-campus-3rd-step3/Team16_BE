package com.kakaotechcampus.team16be.plan.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PlanErrorCode {

  PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "PLAN-001", "일정이 존재하지 않습니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;
}
