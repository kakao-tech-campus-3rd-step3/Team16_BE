package com.kakaotechcampus.team16be.report.dto;

import com.kakaotechcampus.team16be.report.domain.ReportStatus;
import jakarta.validation.constraints.NotNull;

public record ReportResolveRequestDto(
    @NotNull ReportStatus reportStatus
) {
}
