package com.kakaotechcampus.team16be.groundrule;

import com.kakaotechcampus.team16be.groundrule.dto.GroundRuleRequestDto;
import com.kakaotechcampus.team16be.groundrule.dto.GroundRuleResponseDto;
import com.kakaotechcampus.team16be.groundrule.service.GroundRuleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/groups")
public class GroundRuleController {

  private final GroundRuleService groundRuleService;

  public GroundRuleController(GroundRuleService groundRuleService) {
    this.groundRuleService = groundRuleService;
  }

  @PostMapping("/{groupId}/rule") //jpa를 통해 create/update 동시처리.
  public ResponseEntity<GroundRuleResponseDto> addGroupGroundRule(
      @PathVariable Long groupId,
      @RequestBody GroundRuleRequestDto groundRuleRequestDto){
    return ResponseEntity.status(HttpStatus.CREATED).body(groundRuleService.saveGroupGroundRule(groupId, groundRuleRequestDto));
  }

  @GetMapping("/{groupId}/rule")
  public ResponseEntity<GroundRuleResponseDto> getGroupGroundRule(@PathVariable Long groupId){
    return ResponseEntity.status(HttpStatus.OK).body(groundRuleService.getGroupGroundRule(groupId));
  }

  @DeleteMapping("/{groupId}/rule")
  public ResponseEntity<Void> deleteGroupGroundRule(@PathVariable Long groupId){
    groundRuleService.deleteGroupGroundRule(groupId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
