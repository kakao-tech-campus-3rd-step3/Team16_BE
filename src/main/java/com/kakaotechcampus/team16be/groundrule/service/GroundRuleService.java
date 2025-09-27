package com.kakaotechcampus.team16be.groundrule.service;


import com.kakaotechcampus.team16be.groundrule.dto.GroundRuleRequestDto;
import com.kakaotechcampus.team16be.groundrule.dto.GroundRuleResponseDto;
import java.util.List;

public interface GroundRuleService {

  GroundRuleResponseDto saveGroundRule(Long groupId, GroundRuleRequestDto groundRuleRequestDto);
  GroundRuleResponseDto getGroundRule(Long groupId, Long ruleId);
  List<GroundRuleResponseDto> getAllGroundRules(Long groupId);
  GroundRuleResponseDto updateGroundRule(Long groupId, Long ruleId, GroundRuleRequestDto groundRuleRequestDto);
  void deleteGroundRule(Long groupId, Long ruleId);
}
