package com.kakaotechcampus.team16be.planParticipant;

import com.kakaotechcampus.team16be.plan.domain.Plan;
import com.kakaotechcampus.team16be.planParticipant.domain.ParticipantStatus;
import com.kakaotechcampus.team16be.planParticipant.domain.PlanParticipant;
import com.kakaotechcampus.team16be.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanParticipantRepository {

  Optional<PlanParticipant> findByPlanAndUser(Plan plan, User user);
  List<PlanParticipant> findAllByPlanAndParticipant(Plan plan, ParticipantStatus participantStatus);
}
