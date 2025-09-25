package com.kakaotechcampus.team16be.planParticipant.service;

import com.kakaotechcampus.team16be.plan.PlanRepository;
import com.kakaotechcampus.team16be.plan.domain.Plan;
import com.kakaotechcampus.team16be.plan.exception.PlanErrorCode;
import com.kakaotechcampus.team16be.plan.exception.PlanException;
import com.kakaotechcampus.team16be.plan.service.PlanService;
import com.kakaotechcampus.team16be.planParticipant.PlanParticipantRepository;
import com.kakaotechcampus.team16be.planParticipant.domain.PlanParticipant;
import com.kakaotechcampus.team16be.planParticipant.dto.PlanParticipantResponseDto;
import com.kakaotechcampus.team16be.planParticipant.exception.ParticipantErrorCode;
import com.kakaotechcampus.team16be.planParticipant.exception.ParticipantException;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.repository.UserRepository;
import com.kakaotechcampus.team16be.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class PlanParticipantServiceImpl implements PlanParticipantService {

  private final PlanParticipantRepository planParticipantRepository;
  private final UserService userService;
  private final PlanService planService;

  @Override
  public Long attendPlan(Long userId, Long planId) {
    planParticipantRepository.findByUserIdAndPlanId(userId, planId).ifPresent(planParticipant -> {
      throw new ParticipantException(ParticipantErrorCode.PARTICIPANT_NOT_FOUND);
    });

    User user = userService.findById(userId);
    Plan plan = planService.findById(planId);

    PlanParticipant newParticipant = PlanParticipant.create(plan, user);
    planParticipantRepository.save(newParticipant);

    return newParticipant.getId();
  }

  @Override
  public List<PlanParticipantResponseDto> getAllParticipants(Long planId) {
    return planParticipantRepository.findAllByPlanId(planId)
        .stream()
        .map(PlanParticipantResponseDto::from)
        .toList();
  }

  @Override
  public void withdrawAttendance(Long userId, Long planId) {
    PlanParticipant planParticipant = planParticipantRepository.findByUserIdAndPlanId(userId, planId)
        .orElseThrow(() -> new ParticipantException(ParticipantErrorCode.PARTICIPANT_NOT_FOUND));

    planParticipant.withdraw();
  }
}
