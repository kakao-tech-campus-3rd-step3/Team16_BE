package com.kakaotechcampus.team16be.report.controller;

import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.report.domain.TargetType;
import com.kakaotechcampus.team16be.report.dto.ReportRequestDto;
import com.kakaotechcampus.team16be.report.dto.ReportResponseDto;
import com.kakaotechcampus.team16be.report.service.ReportService;
import com.kakaotechcampus.team16be.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "신고 관련 API", description = "일반 유저용 신고 관련 API")
public class UserReportController {

  private final ReportService reportService;

  @Operation(summary = "신고 생성", description = "사용자가 특정 대상(모임, 게시글, 댓글)에 대해 신고를 생성합니다.")
  @PostMapping("/{targetType}/{targetId}")
  public ResponseEntity<ReportResponseDto> createReport(
      @LoginUser User reporter,
      @PathVariable TargetType targetType,
      @PathVariable Long targetId,
      @RequestBody ReportRequestDto reportRequestDto
  ){
    return ResponseEntity.status(HttpStatus.CREATED).body(reportService.createReport(reporter, targetType, targetId, reportRequestDto));
  }

  //READ, UPDATE, DELETE는 관리자용으로 따로 관리
}