package com.kakaotechcampus.team16be.schedulepoll.repository;

import com.kakaotechcampus.team16be.schedulepoll.domain.PollTimeSlot;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollTimeSlotRepository extends JpaRepository<PollTimeSlot, Long> {
  List<PollTimeSlot> findBySchedulePollId(Long schedulePollId);
  List<PollTimeSlot> findBySchedulePollIdOrderByPollDateAscStartTimeAsc(Long schedulePollId);

}
