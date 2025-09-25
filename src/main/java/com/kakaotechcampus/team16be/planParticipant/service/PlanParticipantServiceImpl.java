package com.kakaotechcampus.team16be.planParticipant.service;

import com.kakaotechcampus.team16be.planParticipant.PlanParticipantRepository;
import com.kakaotechcampus.team16be.planParticipant.dto.PlanParticipantResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class PlanParticipantServiceImpl implements PlanParticipantService {

  private final PlanParticipantRepository planParticipantRepository;

  @Override
  public void attendPlan(Long userId, Long planId) {

  }

  @Override
  public List<PlanParticipantResponseDto> getAllParticipants(Long planId) {
    return List.of();
  }

  @Override
  public void cancelAttendance(Long userId, Long planId) {

  }
}
