package com.kakaotechcampus.team16be.groundrule.service;


import com.kakaotechcampus.team16be.groundrule.dto.GroundRuleRequestDto;
import com.kakaotechcampus.team16be.groundrule.dto.GroundRuleResponseDto;

public interface GroundRuleService {

  GroundRuleResponseDto saveGroundRule(Long groupId, GroundRuleRequestDto groundRuleRequestDto);
  GroundRuleResponseDto getGroundRule(Long groupId);
  GroundRuleResponseDto updateGroundRule(Long groupId, GroundRuleRequestDto groundRuleRequestDto);
  void deleteGroundRule(Long groupId);
}
