package com.kakaotechcampus.team16be.plan;

import com.kakaotechcampus.team16be.plan.dto.PlanRequestDto;
import com.kakaotechcampus.team16be.plan.dto.PlanResponseDto;
import com.kakaotechcampus.team16be.plan.service.PlanService;
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
@RequestMapping("/api/plans")
public class PlanController {

  private final PlanService planService;

  @PostMapping
  public ResponseEntity<PlanResponseDto> createPlan(@RequestBody PlanRequestDto planRequestDto){
    return ResponseEntity.status(HttpStatus.CREATED).body(planService.createPlan(planRequestDto));
  }

  @GetMapping("/{planId}")
  public ResponseEntity<PlanResponseDto> getPlan(@PathVariable Long planId){
    return ResponseEntity.status(HttpStatus.OK).body(planService.getPlan(planId));
  }

  @GetMapping
  public ResponseEntity<List<PlanResponseDto>> getAllPlans(){
    return ResponseEntity.status(HttpStatus.OK).body(planService.getAllPlans());
  }

  @PatchMapping("/{planId}")
  public ResponseEntity<PlanResponseDto> updatePlan(
      @PathVariable Long planId,
      @RequestBody PlanRequestDto planRequestDto
  ){
    return ResponseEntity.status(HttpStatus.OK).body(planService.updatePlan(planId, planRequestDto));
  }

  @DeleteMapping("/{planId}")
  public ResponseEntity<Void> deletePlan(@PathVariable Long planId){
    planService.deletePlan(planId);
    return ResponseEntity.noContent().build();
  }
}
