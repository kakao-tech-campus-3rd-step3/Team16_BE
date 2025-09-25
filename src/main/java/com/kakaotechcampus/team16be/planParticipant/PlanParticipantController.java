package com.kakaotechcampus.team16be.planParticipant;

import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.planParticipant.dto.PlanParticipantResponseDto;
import com.kakaotechcampus.team16be.planParticipant.service.PlanParticipantService;
import com.kakaotechcampus.team16be.user.domain.User;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plans/{planId}/participants")
public class PlanParticipantController {

  private final PlanParticipantService planParticipantService;
  @PostMapping
  public ResponseEntity<Void> attendPlan(
      @LoginUser User user,
      @PathVariable Long planId
  ){
    System.out.println("✅ [Controller] attendPlan 호출됨! planId: " + planId);
    Long participantId =  planParticipantService.attendPlan(user.getId(), planId);
    URI location = URI.create(String.format("/api/plans/%d/participant/%d", planId, participantId));
    return ResponseEntity.created(location).build();
  }

  @GetMapping
  public ResponseEntity<List<PlanParticipantResponseDto>> getAllParticipants(@PathVariable Long planId){
    return ResponseEntity.ok(planParticipantService.getAllParticipants(planId));
  }

  @DeleteMapping
  public ResponseEntity<Void> cancelAttendance(
      @LoginUser User user,
      @PathVariable Long planId
  ){
    planParticipantService.withdrawAttendance(user.getId(), planId);
    return ResponseEntity.noContent().build();
  }
}
