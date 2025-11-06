package com.kakaotechcampus.team16be.planParticipant.dto;

import com.kakaotechcampus.team16be.plan.domain.Plan;
import com.kakaotechcampus.team16be.planParticipant.domain.ParticipantStatus;
import com.kakaotechcampus.team16be.planParticipant.domain.PlanParticipant;
import com.kakaotechcampus.team16be.user.domain.User;

public record PlanParticipantResponseDto(
        Long id,
        ParticipantStatus participantStatus,
        SimplePlanDto plan,
        SimpleUserDto user
) {

    public static PlanParticipantResponseDto from(PlanParticipant participant) {
        return new PlanParticipantResponseDto(
                participant.getId(),
                participant.getParticipantStatus(),
                SimplePlanDto.from(participant.getPlan()),
                SimpleUserDto.from(participant.getUser())
        );
    }

    private record SimpleUserDto(
            Long id,
            String nickname
    ) {
        private static SimpleUserDto from(User user) {
            return new SimpleUserDto(user.getId(), user.getNickname());
        }
    }

    private record SimplePlanDto(
            Long id,
            String title
    ) {
        private static SimplePlanDto from(Plan plan) {
            return new SimplePlanDto(plan.getId(), plan.getTitle());
        }
    }
}
