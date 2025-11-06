package com.kakaotechcampus.team16be.plan.service;

import com.kakaotechcampus.team16be.aws.service.S3UploadPresignedUrlService;
import com.kakaotechcampus.team16be.common.eventListener.groupEvent.IncreaseScoreByPlanning;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.plan.PlanRepository;
import com.kakaotechcampus.team16be.plan.domain.Location;
import com.kakaotechcampus.team16be.plan.domain.Plan;
import com.kakaotechcampus.team16be.plan.dto.PlanRequestDto;
import com.kakaotechcampus.team16be.plan.dto.PlanResponseDto;
import com.kakaotechcampus.team16be.plan.exception.PlanErrorCode;
import com.kakaotechcampus.team16be.plan.exception.PlanException;
import com.kakaotechcampus.team16be.planParticipant.service.PlanParticipantService;
import com.kakaotechcampus.team16be.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PlanFacadeServiceImpl {

    final ApplicationEventPublisher eventPublisher;
    private final GroupService groupService;
    private final PlanRepository planRepository;
    private final S3UploadPresignedUrlService s3UploadPresignedUrlService;
    private final PlanParticipantService planParticipantService;



    @Transactional
    public Long createPlan(User user, Long groupId, PlanRequestDto planRequestDto) {

        Group group = groupService.findGroupById(groupId);
        group.checkLeader(user);

        Location location = Location.builder()
                .name(planRequestDto.location().name())
                .latitude(planRequestDto.location().latitude())
                .longitude(planRequestDto.location().longitude())
                .build();

        Plan plan = Plan.builder()
                .group(group)
                .title(planRequestDto.title())
                .description(planRequestDto.description())
                .capacity(planRequestDto.capacity())
                .startTime(planRequestDto.startTime())
                .endTime(planRequestDto.endTime())
                .coverImg(planRequestDto.coverImageUrl())
                .location(location)
                .build();

        Plan savedPlan = planRepository.save(plan);

        eventPublisher.publishEvent(new IncreaseScoreByPlanning(group));
        return savedPlan.getId();
    }

    public List<PlanResponseDto> getAllPlans(Long groupId) {
        return planRepository.findByGroupId(groupId)
                .stream()
                .map(plan -> {
                    Long headCount = planParticipantService.countByPlanId(plan.getId());
                    String fullUrl = (plan.getCoverImg() != null && !plan.getCoverImg().isEmpty())
                            ? s3UploadPresignedUrlService.getUserPublicUrl(plan.getCoverImg())
                            : s3UploadPresignedUrlService.getUserPublicUrl("");
                    return PlanResponseDto.from(plan, fullUrl, headCount);
                })
                .toList();
    }

    public PlanResponseDto getPlan(Long groupId, Long planId) {
        Plan plan = planRepository.findByGroupIdAndId(groupId, planId)
                .orElseThrow(() -> new PlanException(PlanErrorCode.PLAN_NOT_FOUND));

        String fullUrl = (plan.getCoverImg() != null && !plan.getCoverImg().isEmpty())
                ? s3UploadPresignedUrlService.getUserPublicUrl(plan.getCoverImg())
                : s3UploadPresignedUrlService.getUserPublicUrl("");

        Long headCount = planParticipantService.countByPlanId(plan.getId());



        return PlanResponseDto.from(plan, fullUrl, headCount);
    }
}
