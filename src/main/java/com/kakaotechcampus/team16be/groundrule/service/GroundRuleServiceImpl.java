package com.kakaotechcampus.team16be.groundrule.service;

import com.kakaotechcampus.team16be.groundrule.GroundRule;
import com.kakaotechcampus.team16be.groundrule.GroundRuleRepository;
import com.kakaotechcampus.team16be.groundrule.dto.GroundRuleRequestDto;
import com.kakaotechcampus.team16be.groundrule.dto.GroundRuleResponseDto;
import com.kakaotechcampus.team16be.groundrule.exception.GroundRuleErrorCode;
import com.kakaotechcampus.team16be.groundrule.exception.GroundRuleException;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.service.GroupService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroundRuleServiceImpl implements GroundRuleService {

  private final GroundRuleRepository groundRuleRepository;
  private final GroupService groupService;
  private final static int MAX_GROUND_RULES = 5;

  @Override
  @Transactional
  public GroundRuleResponseDto saveGroundRule(Long groupId, GroundRuleRequestDto groundRuleRequestDto) {

    Group group = groupService.findGroupById(groupId);

    if(!canAddGroundRule(groupId)){
      throw new GroundRuleException(GroundRuleErrorCode.RULE_NOT_ADD);
    }

    GroundRule groundRule = GroundRule.create(group, groundRuleRequestDto.content());
    GroundRule saved = groundRuleRepository.save(groundRule);
    return toDto(saved);
  }

  @Override
  public GroundRuleResponseDto getGroundRule(Long groupId, Long ruleId) {

    GroundRule groundRule = findGroundRuleWithValidation(groupId, ruleId);
    return toDto(groundRule);
  }

  @Override
  public List<GroundRuleResponseDto> getAllGroundRules(Long groupId) {

    groupService.findGroupById(groupId);
    return groundRuleRepository.findAllByGroupId(groupId)
        .stream()
        .map(this::toDto)
        .toList();
  }

  @Override
  @Transactional
  public GroundRuleResponseDto updateGroundRule(Long groupId, Long ruleId, GroundRuleRequestDto groundRuleRequestDto) {

    GroundRule groundRule = findGroundRuleWithValidation(groupId, ruleId);
    groundRule.changeContent(groundRuleRequestDto.content());
    return toDto(groundRule);
  }

  @Override
  @Transactional
  public void deleteGroundRule(Long groupId, Long ruleId) {

    GroundRule groundRule = findGroundRuleWithValidation(groupId, ruleId);
    groundRuleRepository.delete(groundRule);
  }

  private GroundRuleResponseDto toDto(GroundRule groundRule) {

    return new GroundRuleResponseDto(
        groundRule.getId(),
        groundRule.getContent(),
        groundRule.getCreatedAt(),
        groundRule.getUpdatedAt()
    );
  }

  private GroundRule findGroundRuleWithValidation(Long groupId, Long ruleId){

    GroundRule groundRule =  groundRuleRepository.findById(ruleId)
                                                 .orElseThrow(() -> new GroundRuleException(GroundRuleErrorCode.RULE_NOT_FOUND));

    groundRule.validateAccess(groupId);
    return groundRule;
  }

  private boolean canAddGroundRule(Long groupId){
    long count = groundRuleRepository.countByGroupId(groupId);
    return count < MAX_GROUND_RULES;
  }
}
