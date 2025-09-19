package com.kakaotechcampus.team16be.report.controller;

import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.report.dto.ReportResolveRequestDto;
import com.kakaotechcampus.team16be.report.dto.ReportResponseDto;
import com.kakaotechcampus.team16be.report.service.ReportService;
import com.kakaotechcampus.team16be.user.domain.User;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/admin/reports")
@RequiredArgsConstructor
@Tag(name = "신고 관련 API", description = "어드민용 신고 관리 API")
public class AdminReportController {

  private final ReportService reportService;

  // 신고 기능의 R&U&D는 관리자만 접근할 수 있게끔 유도
  @Operation(summary = "신고 처리", description = "들어온 신고의 상태를 변경합니다. (신고 처리)")
  @PatchMapping("/{reportId}")
  public ResponseEntity<ReportResponseDto> resolveReport(
      @PathVariable Long reportId,
      @LoginUser User adminUser,
      @RequestBody ReportResolveRequestDto reportResolveRequestDto
  ){
    return ResponseEntity.ok(reportService.resolveReport(reportId, adminUser, reportResolveRequestDto));
  }

  @Operation(summary = "모든 신고 조회", description = "모든 신고 목록을 조회합니다.")
  @GetMapping
  public ResponseEntity<List<ReportResponseDto>> getAllReports(@LoginUser User adminUser){
    return ResponseEntity.ok(reportService.getAllReports());
  }

  @Operation(summary = "특정 신고 조회", description = "단일 신고 내용을 조회합니다.")
  @GetMapping("/{reportId}")
  public ResponseEntity<ReportResponseDto> getReport(
      @PathVariable Long reportId,
      @LoginUser User adminUser
  ){
    return ResponseEntity.ok(reportService.getReport(reportId));
  }

  @Operation(summary = "신고 삭제", description = "특정 신고를 삭제합니다.")
  @DeleteMapping("/{reportId}")
  public ResponseEntity<Void> deleteReport(
      @PathVariable Long reportId,
      @LoginUser User adminUser
  ){
    reportService.deleteReport(reportId);
    return ResponseEntity.noContent().build();
  }
}
