package com.kakaotechcampus.team16be.groundrule.service;

import com.kakaotechcampus.team16be.groundrule.GroundRule;
import com.kakaotechcampus.team16be.groundrule.GroundRuleRepository;
import com.kakaotechcampus.team16be.groundrule.dto.GroundRuleRequestDto;
import com.kakaotechcampus.team16be.groundrule.dto.GroundRuleResponseDto;
import com.kakaotechcampus.team16be.groundrule.exception.GroundRuleErrorCode;
import com.kakaotechcampus.team16be.groundrule.exception.GroundRuleException;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.exception.GroupErrorCode;
import com.kakaotechcampus.team16be.group.exception.GroupException;
import com.kakaotechcampus.team16be.group.repository.GroupRepository;
import com.kakaotechcampus.team16be.group.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroundRuleServiceImpl implements GroundRuleService {

  private final GroundRuleRepository groundRuleRepository;
  private final GroupService groupService;

  @Override
  @Transactional
  public GroundRuleResponseDto saveGroundRule(Long groupId, GroundRuleRequestDto groundRuleRequestDto) {
    Group group = groupService.findGroupById(groupId);
    GroundRule groundRule = GroundRule.create(group, groundRuleRequestDto.content());

    GroundRule saved = groundRuleRepository.save(groundRule);
    return toDto(saved);
  }

  @Override
  public GroundRuleResponseDto getGroundRule(Long groupId, Long ruleId) {
    GroundRule groundRule = groundRuleRepository.findById(ruleId)
        .orElseThrow(() -> new GroundRuleException(GroundRuleErrorCode.RULE_NOT_FOUND));

    groundRule.validateAccess(groupId);
    return toDto(groundRule);
  }

  @Override
  @Transactional
  public GroundRuleResponseDto updateGroundRule(Long groupId, Long ruleId, GroundRuleRequestDto groundRuleRequestDto) {
    GroundRule groundRule = groundRuleRepository.findById(ruleId)
                                                .orElseThrow(() -> new GroundRuleException(GroundRuleErrorCode.RULE_NOT_FOUND));

    groundRule.validateAccess(groupId);
    groundRule.changeContent(groundRuleRequestDto.content());
    return toDto(groundRule);
  }

  @Override
  @Transactional
  public void deleteGroundRule(Long groupId, Long ruleId) {
    GroundRule groundRule = groundRuleRepository.findById(ruleId)
                                                .orElseThrow(() -> new GroundRuleException(GroundRuleErrorCode.RULE_NOT_FOUND));

    groundRule.validateAccess(groupId);
    groundRuleRepository.delete(groundRule);
  }

  private GroundRuleResponseDto toDto(GroundRule groundRule) {
    return new GroundRuleResponseDto(
        groundRule.getContent(),
        groundRule.getCreatedAt(),
        groundRule.getUpdatedAt()
    );
  }


}
