package com.kakaotechcampus.team16be.planParticipant;

import com.kakaotechcampus.team16be.plan.domain.Plan;
import com.kakaotechcampus.team16be.planParticipant.domain.PlanParticipant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanParticipantRepository extends JpaRepository<PlanParticipant, Long> {

    Optional<PlanParticipant> findByUserIdAndPlanId(Long userId, Long planId);

    @Query("SELECT pp FROM PlanParticipant pp WHERE pp.plan.id = :planId AND pp.participantStatus = 'ATTENDING'")
    List<PlanParticipant> findAllByPlanId(@Param("planId") Long planId);

    @Query("SELECT COUNT(pp) FROM PlanParticipant pp WHERE pp.plan.id = :planId AND pp.participantStatus = 'ATTENDING'")
    Long countByPlanId(@Param("planId") Long planId);

    void deleteAllByUserId(Long userId);

    List<PlanParticipant> findAllByPlan(Plan plan);
}
