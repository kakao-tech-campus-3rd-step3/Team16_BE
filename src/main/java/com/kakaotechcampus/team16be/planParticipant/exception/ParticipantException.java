package com.kakaotechcampus.team16be.planParticipant.exception;

import lombok.Getter;

@Getter
public class ParticipantException extends RuntimeException {

  private final ParticipantErrorCode participantErrorCode;
  public ParticipantException(ParticipantErrorCode participantErrorCode) {
    super(participantErrorCode.getMessage());
    this.participantErrorCode = participantErrorCode;
  }
}
