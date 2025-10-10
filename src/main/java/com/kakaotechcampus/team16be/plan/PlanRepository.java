package com.kakaotechcampus.team16be.plan;

import com.kakaotechcampus.team16be.plan.domain.Plan;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

  Optional<Plan> findByGroupIdAndId(Long groupId, Long planId);

  List<Plan> findByGroupId(Long groupId);

    List<Plan> findAllByEndTimeBetween(LocalDateTime start, LocalDateTime end);
}
