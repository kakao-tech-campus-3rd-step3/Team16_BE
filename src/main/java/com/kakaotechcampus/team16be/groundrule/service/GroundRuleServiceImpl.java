package com.kakaotechcampus.team16be.groundrule.service;

import com.kakaotechcampus.team16be.groundrule.GroundRule;
import com.kakaotechcampus.team16be.groundrule.GroundRuleRepository;
import com.kakaotechcampus.team16be.groundrule.dto.GroundRuleRequestDto;
import com.kakaotechcampus.team16be.groundrule.dto.GroundRuleResponseDto;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.exception.ErrorCode;
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
  private final GroupRepository groupRepository;

  @Override
  @Transactional
  public GroundRuleResponseDto saveGroundRule(Long groupId, GroundRuleRequestDto groundRuleRequestDto) {
    Group group = groupService.findGroupById(groupId);
    GroundRule groundRule = GroundRule.create(group, groundRuleRequestDto.content());

    GroundRule saved = groundRuleRepository.save(groundRule);
    return toDto(saved);
  }

  @Override
  public GroundRuleResponseDto getGroundRule(Long groupId) {
    Group group = groupService.findGroupById(groupId);

    return groundRuleRepository.findById(groupId)
                               .map(this::toDto)
                               .orElse(GroundRuleResponseDto.empty());
  }

  @Override
  @Transactional
  public GroundRuleResponseDto updateGroundRule(Long groupId, GroundRuleRequestDto groundRuleRequestDto) {
    if (!groupRepository.existsById(groupId)) {
      throw new GroupException(ErrorCode.GROUP_CANNOT_FOUND);
    }

    GroundRule groundRule = groundRuleRepository.findByGroupId(groupId)
                                                .orElseThrow(() -> new IllegalArgumentException("그라운드 룰이 존재하지 않습니다."));

    groundRule.changeContent(groundRuleRequestDto.content());
    return toDto(groundRule);
  }

  @Override
  @Transactional
  public void deleteGroundRule(Long groupId) {
    groundRuleRepository.deleteById(groupId);
  }

  private GroundRuleResponseDto toDto(GroundRule groundRule) {
    return new GroundRuleResponseDto(
        groundRule.getContent(),
        groundRule.getCreatedAt() != null ? groundRule.getCreatedAt().toString() : null,
        groundRule.getUpdatedAt() != null ? groundRule.getUpdatedAt().toString() : null
    );
  }
}
