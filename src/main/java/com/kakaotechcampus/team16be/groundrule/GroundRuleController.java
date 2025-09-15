package com.kakaotechcampus.team16be.groundrule;

import com.kakaotechcampus.team16be.groundrule.dto.GroundRuleRequestDto;
import com.kakaotechcampus.team16be.groundrule.dto.GroundRuleResponseDto;
import com.kakaotechcampus.team16be.groundrule.service.GroundRuleService;
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
public class GroundRuleController {

  private final GroundRuleService groundRuleService;

  @PostMapping("/{groupId}/rule")
  public ResponseEntity<GroundRuleResponseDto> addGroundRule(
      @PathVariable Long groupId,
      @RequestBody GroundRuleRequestDto groundRuleRequestDto) {
    return ResponseEntity.status(HttpStatus.CREATED)
                         .body(groundRuleService.saveGroundRule(groupId, groundRuleRequestDto));
  }

  @PutMapping("/{groupId}/rule/{ruleId}")
  public ResponseEntity<GroundRuleResponseDto> updateGroundRule(
      @PathVariable Long groupId,
      @PathVariable Long ruleId,
      @RequestBody GroundRuleRequestDto groundRuleRequestDto) {
    return ResponseEntity.ok(groundRuleService.updateGroundRule(groupId, ruleId, groundRuleRequestDto));
  }

  @GetMapping("/{groupId}/rule/{ruleId}")
  public ResponseEntity<GroundRuleResponseDto> getGroundRule(
      @PathVariable Long groupId,
      @PathVariable Long ruleId
  ) {
    return ResponseEntity.ok(groundRuleService.getGroundRule(groupId, ruleId));
  }

  @DeleteMapping("/{groupId}/rule/{ruleId}")
  public ResponseEntity<Void> deleteGroundRule(
      @PathVariable Long groupId,
      @PathVariable Long ruleId
    ) {
    groundRuleService.deleteGroundRule(groupId, ruleId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
