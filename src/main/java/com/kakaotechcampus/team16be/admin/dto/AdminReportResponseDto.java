package com.kakaotechcampus.team16be.admin.dto;

import com.kakaotechcampus.team16be.report.domain.Report;
import lombok.Getter;

@Getter
public class AdminReportResponseDto {
    private final Long id;
    private final String reporterNickname;
    private final String reason;
    private final String status;
    private final String targetType;
    private final Long targetId;
    private final String createdAt;

    private AdminReportResponseDto(Long id, String reporterNickname, String reason, String status,
                                   String targetType, Long targetId, String createdAt) {
        this.id = id;
        this.reporterNickname = reporterNickname;
        this.reason = reason;
        this.status = status;
        this.targetType = targetType;
        this.targetId = targetId;
        this.createdAt = createdAt;
    }

    public static AdminReportResponseDto from(Report report) {
        return new AdminReportResponseDto(
                report.getId(),
                report.getReporter().getNickname(),
                report.getReason(),
                report.getStatus().name(),
                report.getTargetType().name(),
                report.getTargetId(),
                report.getCreatedAt().toString()
        );
    }
}
