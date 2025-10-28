package com.kakaotechcampus.team16be.schedulepoll.repository;

import com.kakaotechcampus.team16be.schedulepoll.domain.PollVote;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PollVoteRepository extends JpaRepository<PollVote, Long> {
  List<PollVote> findByTimeSlotId(Long timeSlot);

  @Query("SELECT v FROM PollVote v WHERE v.timeSlot.schedulePoll.id = :pollId AND v.user.id = :userId")
  List<PollVote> findBySchedulePollIdAndUserId(@Param("pollId") Long pollId, @Param("userId") Long userId);

  @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM PollVote v WHERE v.timeSlot.schedulePoll.id = :pollId AND v.user.id = :userId")
  boolean existsBySchedulePollIdAndUserId(@Param("pollId") Long pollId, @Param("userId") Long userId);

  // 재투표 용
  @Modifying(flushAutomatically = true, clearAutomatically = true)
  @Query("DELETE FROM PollVote v WHERE v.timeSlot.schedulePoll.id = :pollId AND v.user.id = :userId")
  void deleteBySchedulePollIdAndUserId(@Param("pollId") Long pollId, @Param("userId") Long userId);
}
