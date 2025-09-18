package com.kakaotechcampus.team16be.groundrule;

import com.kakaotechcampus.team16be.groundrule.dto.GroundRuleRequestDto;
import com.kakaotechcampus.team16be.groundrule.dto.GroundRuleResponseDto;
import com.kakaotechcampus.team16be.groundrule.service.GroundRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
@Tag(name = "그라운드룰 API", description = "그라운드룰 관련 API")
public class GroundRuleController {

  private final GroundRuleService groundRuleService;

  @Operation(summary = "그라운드룰 등록", description = "특정 모임에 새로운 그라운드룰을 등록합니다.")
  @PostMapping("/{groupId}/rule")
  public ResponseEntity<GroundRuleResponseDto> addGroundRule(
      @PathVariable Long groupId,
      @RequestBody GroundRuleRequestDto groundRuleRequestDto) {
    return ResponseEntity.status(HttpStatus.CREATED)
                         .body(groundRuleService.saveGroundRule(groupId, groundRuleRequestDto));
  }

  @Operation(summary = "그라운드룰 수정", description = "특정 모임의 기존 그라운드룰을 수정합니다.")
  @PutMapping("/{groupId}/rule/{ruleId}")
  public ResponseEntity<GroundRuleResponseDto> updateGroundRule(
      @PathVariable Long groupId,
      @PathVariable Long ruleId,
      @RequestBody GroundRuleRequestDto groundRuleRequestDto) {
    return ResponseEntity.ok(groundRuleService.updateGroundRule(groupId, ruleId, groundRuleRequestDto));
  }

  @Operation(summary = "그라운드룰 조회", description = "특정 모임의 그라운드룰을 조회합니다.")
  @GetMapping("/{groupId}/rule/{ruleId}")
  public ResponseEntity<GroundRuleResponseDto> getGroundRule(
      @PathVariable Long groupId,
      @PathVariable Long ruleId
  ) {
    return ResponseEntity.ok(groundRuleService.getGroundRule(groupId, ruleId));
  }

  @Operation(summary = "그라운드룰 삭제", description = "특정 모임의 그라운드룰을 삭제합니다.")
  @DeleteMapping("/{groupId}/rule/{ruleId}")
  public ResponseEntity<Void> deleteGroundRule(
      @PathVariable Long groupId,
      @PathVariable Long ruleId
    ) {
    groundRuleService.deleteGroundRule(groupId, ruleId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
