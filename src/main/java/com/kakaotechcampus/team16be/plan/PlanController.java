package com.kakaotechcampus.team16be.plan;

import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.plan.dto.PlanRequestDto;
import com.kakaotechcampus.team16be.plan.dto.PlanResponseDto;
import com.kakaotechcampus.team16be.plan.service.PlanService;
import com.kakaotechcampus.team16be.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PlanController {

  private final PlanService planService;

  @PostMapping("/groups/{groupId}/plans")
  public ResponseEntity<PlanResponseDto> createPlan(
      @LoginUser User user,
      @PathVariable Long groupId,
      @RequestBody PlanRequestDto planRequestDto){
    return ResponseEntity.status(HttpStatus.CREATED).body(planService.createPlan(user, groupId, planRequestDto));
  }

  @GetMapping("/groups/{groupId}/plans/{planId}")
  public ResponseEntity<PlanResponseDto> getPlan(
      @PathVariable Long groupId,
      @PathVariable Long planId){
    return ResponseEntity.status(HttpStatus.OK).body(planService.getPlan(groupId, planId));
  }

  @GetMapping("/groups/{groupId}/plans")
  public ResponseEntity<List<PlanResponseDto>> getAllPlans(
      @PathVariable Long groupId
  ){
    return ResponseEntity.status(HttpStatus.OK).body(planService.getAllPlans(groupId));
  }

  @PatchMapping("/groups/{groupId}/plans/{planId}")
  public ResponseEntity<PlanResponseDto> updatePlan(
      @LoginUser User user,
      @PathVariable Long groupId,
      @PathVariable Long planId,
      @RequestBody PlanRequestDto planRequestDto
  ){
    return ResponseEntity.status(HttpStatus.OK).body(planService.updatePlan(user, groupId, planId, planRequestDto));
  }

  @DeleteMapping("/groups/{groupId}/plans/{planId}")
  public ResponseEntity<Void> deletePlan(
      @LoginUser User user,
      @PathVariable Long groupId,
      @PathVariable Long planId){
    planService.deletePlan(user, groupId, planId);
    return ResponseEntity.noContent().build();
  }
}
