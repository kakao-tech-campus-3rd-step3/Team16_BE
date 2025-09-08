package com.kakaotechcampus.team16be.groundrule.service;

import com.kakaotechcampus.team16be.groundrule.GroundRule;
import com.kakaotechcampus.team16be.groundrule.GroundRuleRepository;
import com.kakaotechcampus.team16be.groundrule.dto.GroundRuleRequestDto;
import com.kakaotechcampus.team16be.groundrule.dto.GroundRuleResponseDto;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroundRuleServiceImpl implements GroundRuleService{

  private final GroundRuleRepository groundRuleRepository;
  private final GroupService groupService; //후에 코드 병합 후 이용

  @Override// 모임 코드와 연동 예정
  @Transactional
  public GroundRuleResponseDto saveGroupGroundRule(Long groupId,
      GroundRuleRequestDto groundRuleRequestDto) {
    Group group = groupService.findGroupById(groupId);

    GroundRule groundRule = groundRuleRepository.findById(groupId)
        .map(existing -> {
          existing.changeContent(groundRuleRequestDto.content());
          return existing;
        })
        .orElseGet(() -> GroundRule.create(group, groundRuleRequestDto.content()));

    GroundRule saved = groundRuleRepository.save(groundRule);
    return toDto(saved);
  }

  @Override
  public GroundRuleResponseDto getGroupGroundRule(Long groupId) {
    Group group = groupService.findGroupById(groupId);

    return groundRuleRepository.findById(groupId)
        .map(this::toDto)
        .orElseGet(() -> new GroundRuleResponseDto("", null, null));
  }

  @Override
  @Transactional
  public void deleteGroupGroundRule(Long groupId) {
    groundRuleRepository.deleteById(groupId);
  }

  private GroundRuleResponseDto toDto(GroundRule groundRule){
    return new GroundRuleResponseDto(
        groundRule.getContent(),
        groundRule.toString(),
        groundRule.toString()
    );
  }
}
