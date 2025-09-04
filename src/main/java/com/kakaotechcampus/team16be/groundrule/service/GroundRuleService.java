package com.kakaotechcampus.team16be.groundrule.service;


import com.kakaotechcampus.team16be.groundrule.dto.GroundRuleRequestDto;
import com.kakaotechcampus.team16be.groundrule.dto.GroundRuleResponseDto;

public interface GroundRuleService {

  GroundRuleResponseDto saveGroupGroundRule(Long groupId, GroundRuleRequestDto groundRuleRequestDto);
  GroundRuleResponseDto getGroupGroundRule(Long groupId);
  void deleteGroupGroundRule(Long groupId);
}
