package com.kakaotechcampus.team16be.planParticipant.service;

import com.kakaotechcampus.team16be.attend.domain.Attend;
import com.kakaotechcampus.team16be.attend.repository.AttendRepository;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.groupMember.service.GroupMemberService;
import com.kakaotechcampus.team16be.plan.domain.Plan;
import com.kakaotechcampus.team16be.plan.service.PlanService;
import com.kakaotechcampus.team16be.planParticipant.PlanParticipantRepository;
import com.kakaotechcampus.team16be.planParticipant.domain.PlanParticipant;
import com.kakaotechcampus.team16be.planParticipant.dto.PlanParticipantResponseDto;
import com.kakaotechcampus.team16be.planParticipant.exception.ParticipantErrorCode;
import com.kakaotechcampus.team16be.planParticipant.exception.ParticipantException;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.service.UserService;
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
    private final AttendRepository attendRepository;
    private final GroupMemberService groupMemberService;


    @Override
    public Long attendPlan(Long userId, Long planId) {
        planParticipantRepository.findByUserIdAndPlanId(userId, planId).ifPresent(planParticipant -> {
            throw new ParticipantException(ParticipantErrorCode.ALREADY_PARTICIPATING);
        });

        User user = userService.findById(userId);
        Plan plan = planService.findById(planId);

        Long headCount = planParticipantRepository.countByPlanId(plan.getId());
        
        if (headCount >= plan.getCapacity()) {
            throw new ParticipantException(ParticipantErrorCode.PLAN_CAPACITY_EXCEEDED);
        }

        PlanParticipant newParticipant = PlanParticipant.create(plan, user);
        planParticipantRepository.save(newParticipant);

        GroupMember groupMember = groupMemberService.findByGroupAndUser(plan.getGroup(), user);
        Attend attend = Attend.attendPlanHolding(groupMember, plan);
        attendRepository.save(attend);

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

    @Override
    public List<PlanParticipant> findAllByPlan(Plan plan) {

        return planParticipantRepository.findAllByPlan(plan);
    }

    @Override
    public Long countByPlanId(Long id) {
        return planParticipantRepository.countByPlanId(id);
    }
}
