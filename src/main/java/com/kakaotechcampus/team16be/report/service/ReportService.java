package com.kakaotechcampus.team16be.report.service;

import com.kakaotechcampus.team16be.report.domain.TargetType;
import com.kakaotechcampus.team16be.report.dto.ReportRequestDto;
import com.kakaotechcampus.team16be.report.dto.ReportResolveRequestDto;
import com.kakaotechcampus.team16be.report.dto.ReportResponseDto;
import com.kakaotechcampus.team16be.user.domain.User;
import java.util.List;

public interface ReportService {
    //일반 사용자용 메서드
    ReportResponseDto createReport(User reporter, TargetType targetType, Long targetId,
                                   ReportRequestDto reportRequestDto);

    //관리자용 메서드
    ReportResponseDto getReport(Long reportId);

    List<ReportResponseDto> getAllReports();

    ReportResponseDto resolveReport(Long reportId, User adminUser, ReportResolveRequestDto reportResolveRequestDto);

    void deleteReport(Long reportId);
}
