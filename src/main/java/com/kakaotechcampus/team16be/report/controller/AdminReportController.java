package com.kakaotechcampus.team16be.report.controller;

import com.kakaotechcampus.team16be.common.annotation.AdminOnly;
import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.report.dto.ReportRequestDto;
import com.kakaotechcampus.team16be.report.dto.ReportResolveRequestDto;
import com.kakaotechcampus.team16be.report.dto.ReportResponseDto;
import com.kakaotechcampus.team16be.report.service.ReportService;
import com.kakaotechcampus.team16be.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class AdminReportController {

  private final ReportService reportService;

  // 신고 기능의 R&U&D는 관리자만 접근할 수 있게끔 유도
  @AdminOnly
  @PatchMapping("/{reportId}")
  public ResponseEntity<ReportResponseDto> resolveReport(
      @PathVariable Long reportId,
      @LoginUser User adminUser,
      @RequestBody ReportResolveRequestDto reportResolveRequestDto
  ){
    return ResponseEntity.ok(reportService.resolveReport(reportId, adminUser, reportResolveRequestDto));
  }

  @AdminOnly
  @GetMapping
  public ResponseEntity<List<ReportResponseDto>> getAllReports(@LoginUser User adminUser){
    return ResponseEntity.ok(reportService.getAllReports());
  }

  @AdminOnly
  @GetMapping("/{reportId}")
  public ResponseEntity<ReportResponseDto> getReport(
      @PathVariable Long reportId,
      @LoginUser User adminUser
  ){
    return ResponseEntity.ok(reportService.getReport(reportId));
  }

  @AdminOnly
  @DeleteMapping("/{reportId}")
  public ResponseEntity<Void> deleteReport(
      @PathVariable Long reportId,
      @LoginUser User adminUser
  ){
    reportService.deleteReport(reportId);
    return ResponseEntity.noContent().build();
  }
}
