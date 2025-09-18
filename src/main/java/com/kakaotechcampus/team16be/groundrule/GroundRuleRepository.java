package com.kakaotechcampus.team16be.groundrule;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroundRuleRepository extends JpaRepository<GroundRule, Long>{
  Optional<GroundRule> findByGroupId(Long groupId);
  List<GroundRule> findAllByGroupId(Long groupId);
}
