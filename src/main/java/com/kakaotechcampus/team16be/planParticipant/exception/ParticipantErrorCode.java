package com.kakaotechcampus.team16be.planParticipant.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ParticipantErrorCode {

  PARTICIPANT_NOT_FOUND(HttpStatus.NOT_FOUND, "PARTICIPANT-001", "이미 참가신청 하였습니다.");

  private final HttpStatus httpStatus;
  private final String code;
  private final String message;
}
