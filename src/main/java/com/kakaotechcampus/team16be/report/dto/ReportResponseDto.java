package com.kakaotechcampus.team16be.report.dto;

import com.kakaotechcampus.team16be.report.domain.Report.ReasonCode;
import com.kakaotechcampus.team16be.report.domain.ReportStatus;
import com.kakaotechcampus.team16be.report.domain.TargetType;

public record ReportResponseDto(
        Long id,
        Long reporterId,
        TargetType targetType,
        Long targetId,
        ReasonCode reasonCode,
        String reason,
        ReportStatus status,
        String createdAt,
        String updatedAt
) {
}
