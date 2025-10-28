package com.kakaotechcampus.team16be.schedulepoll.repository;

import com.kakaotechcampus.team16be.schedulepoll.domain.SchedulePoll;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchedulePollRepository extends JpaRepository<SchedulePoll, Long> {
  List<SchedulePoll> findByGroupId(Long groupId);
  List<SchedulePoll> findByGroupIdAndIsClosedFalse(Long groupId);

}
