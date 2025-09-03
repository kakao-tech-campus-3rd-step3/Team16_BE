package com.kakaotechcampus.team16be.groundrule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroundRuleRepository extends JpaRepository<GroundRule, Long> {
}
