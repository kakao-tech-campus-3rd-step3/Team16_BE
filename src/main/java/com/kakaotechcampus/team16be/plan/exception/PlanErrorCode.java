package com.kakaotechcampus.team16be.plan.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PlanErrorCode {

  PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "PLAN-001", "일정이 존재하지 않습니다."),
  INVALID_CAPACITY(HttpStatus.BAD_REQUEST, "PLAN-002", "참여 인원은 1명 이상이어야 합니다."),
  TIME_REQUIRED(HttpStatus.BAD_REQUEST, "PLAN-003", "시작 및 종료 시간은 필수입니다."),
  INVALID_TIME_RANGE(HttpStatus.BAD_REQUEST, "PLAN-004", "시작 시간이 종료 시간 이후일 수 없습니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;
}
