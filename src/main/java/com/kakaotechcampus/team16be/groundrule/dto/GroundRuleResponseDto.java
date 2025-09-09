package com.kakaotechcampus.team16be.groundrule.dto;

public record GroundRuleResponseDto(
    String content,
    String createdAt,
    String updatedAt
) {

  public static GroundRuleResponseDto empty(){
    return new GroundRuleResponseDto("", null, null);
  }
}
