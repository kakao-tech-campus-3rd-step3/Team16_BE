package com.kakaotechcampus.team16be.report.dto;


import com.kakaotechcampus.team16be.report.domain.Report;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReportRequestDto(
        @NotNull Report.ReasonCode reasonCode,
        @Size(max = 500) String reason // 프론트와 협의
) {
}
