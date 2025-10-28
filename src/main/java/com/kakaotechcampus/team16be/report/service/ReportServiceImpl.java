package com.kakaotechcampus.team16be.report.service;


import com.kakaotechcampus.team16be.common.eventListener.ReportResolvedEvent;
import com.kakaotechcampus.team16be.report.ReportRepository;
import com.kakaotechcampus.team16be.report.domain.TargetType;
import com.kakaotechcampus.team16be.report.dto.ReportRequestDto;
import com.kakaotechcampus.team16be.report.dto.ReportResolveRequestDto;
import com.kakaotechcampus.team16be.report.dto.ReportResponseDto;
import com.kakaotechcampus.team16be.report.domain.Report;
import com.kakaotechcampus.team16be.report.exception.ReportErrorCode;
import com.kakaotechcampus.team16be.report.exception.ReportException;
import com.kakaotechcampus.team16be.user.domain.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService{

  private final ReportRepository reportRepository;
    private final ApplicationEventPublisher eventPublisher;


  @Override
  @Transactional
  public ReportResponseDto createReport(User reporter, TargetType targetType, Long targetId,
      ReportRequestDto reportRequestDto) {

    Report report = Report.builder()
        .reporter(reporter)
        .targetType(targetType)
        .targetId(targetId)
        .reasonCode(reportRequestDto.reasonCode())
        .reason(reportRequestDto.reason())
        .build();

    eventPublisher.publishEvent(new ReportResolvedEvent(report));

    Report saved = reportRepository.save(report);
    return toDto(saved);
  }

  @Override
  public ReportResponseDto getReport(Long reportId) {
    Report report =  reportRepository.findById(reportId)
        .orElseThrow(() -> new ReportException(ReportErrorCode.REPORT_NOT_FOUND));

    return toDto(report);
  }

  @Override
  public List<ReportResponseDto> getAllReports() {
    return reportRepository.findAll()
        .stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public ReportResponseDto resolveReport(Long reportId, User adminUser,
      ReportResolveRequestDto reportResolveRequestDto) {

    Report report = reportRepository.findById(reportId)
        .orElseThrow(() -> new ReportException(ReportErrorCode.REPORT_NOT_FOUND));

    report.resolve(adminUser, reportResolveRequestDto.reportStatus());

    return toDto(report);
  }

  @Override
  @Transactional
  public void deleteReport(Long reportId) {
    Report report = reportRepository.findById(reportId)
        .orElseThrow(() -> new ReportException(ReportErrorCode.REPORT_NOT_FOUND));
    reportRepository.delete(report);
  }

  private ReportResponseDto toDto(Report report){
    return new ReportResponseDto(
      report.getId(),
      report.getReporter().getId(),
      report.getTargetType(),
      report.getTargetId(),
      report.getReasonCode(),
      report.getReason(),
      report.getStatus(), report.getCreatedAt() != null ? report.getCreatedAt().toString() : null, report.getUpdatedAt() != null ? report.getUpdatedAt().toString() : null
    );
  }
}
