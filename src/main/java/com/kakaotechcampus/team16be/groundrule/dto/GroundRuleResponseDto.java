package com.kakaotechcampus.team16be.groundrule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record GroundRuleResponseDto(
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

  public static GroundRuleResponseDto empty(){
    return new GroundRuleResponseDto("", null, null);
  }
}
