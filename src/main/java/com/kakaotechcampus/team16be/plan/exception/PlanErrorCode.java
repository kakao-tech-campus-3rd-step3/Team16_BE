package com.kakaotechcampus.team16be.plan.exception;

import com.kakaotechcampus.team16be.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PlanErrorCode implements ErrorCode {

  // 기본 에러
  PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "PLAN-001", "일정이 존재하지 않습니다."),

  // 인원수 관련
  INVALID_CAPACITY(HttpStatus.BAD_REQUEST, "PLAN-002", "모집 정원은 1명 이상이어야 합니다."),
  INVALID_ATTENDEE_COUNT(HttpStatus.BAD_REQUEST, "PLAN-003", "참석자 수는 0명 이상이어야 합니다."),
  ATTENDEE_EXCEEDS_CAPACITY(HttpStatus.BAD_REQUEST, "PLAN-004", "참석자 수가 모집 정원을 초과할 수 없습니다."),
  PLAN_FULL(HttpStatus.BAD_REQUEST, "PLAN-005", "일정이 가득 찼습니다. 더 이상 참석할 수 없습니다."),
  NO_ATTENDEE_TO_REMOVE(HttpStatus.BAD_REQUEST, "PLAN-006", "참석 취소할 인원이 없습니다."),

  // 시간 관련
  TIME_REQUIRED(HttpStatus.BAD_REQUEST, "PLAN-007", "시작시간과 종료시간은 필수입니다."),
  INVALID_TIME_RANGE(HttpStatus.BAD_REQUEST, "PLAN-008", "종료시간은 시작시간보다 늦어야 합니다."),

  // 좌표 관련
  COORDINATES_REQUIRED(HttpStatus.BAD_REQUEST, "PLAN-009", "위도와 경도는 필수입니다."),
  INVALID_COORDINATE(HttpStatus.BAD_REQUEST, "PLAN-010", "좌표 값이 범위를 벗어났습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;
}