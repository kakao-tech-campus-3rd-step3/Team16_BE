package com.kakaotechcampus.team16be.groundrule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record GroundRuleResponseDto(
    Long id,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
