package com.kakaotechcampus.team16be.planParticipant;

import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.planParticipant.dto.PlanParticipantResponseDto;
import com.kakaotechcampus.team16be.planParticipant.service.PlanParticipantService;
import com.kakaotechcampus.team16be.user.domain.User;
import java.net.URI;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plans/{planId}/participants")
@Tag(name = "일정 참여자", description = "일정 참여 관련 API")
public class PlanParticipantController {

  private final PlanParticipantService planParticipantService;

  @Operation(summary = "일정 참여", description = "로그인한 유저가 특정 일정에 참여합니다.")
  @PostMapping
  public ResponseEntity<Void> attendPlan(
      @LoginUser User user,
      @PathVariable Long planId
  ){
    Long participantId =  planParticipantService.attendPlan(user.getId(), planId);
    URI location = URI.create(String.format("/api/plans/%d/participant/%d", planId, participantId));
    return ResponseEntity.created(location).build();
  }

  @Operation(summary = "일정 참여자 조회", description = "특정 일정에 참여한 모든 유저를 조회합니다.")
  @GetMapping
  public ResponseEntity<List<PlanParticipantResponseDto>> getAllParticipants(@PathVariable Long planId){
    return ResponseEntity.ok(planParticipantService.getAllParticipants(planId));
  }

  @Operation(summary = "일정 참여 취소", description = "로그인한 유저가 특정 일정에 대한 참여를 취소합니다.")
  @PatchMapping
  public ResponseEntity<Void> withdrawAttendance(
      @LoginUser User user,
      @PathVariable Long planId
  ){
    planParticipantService.withdrawAttendance(user.getId(), planId);
    return ResponseEntity.noContent().build();
  }
}
