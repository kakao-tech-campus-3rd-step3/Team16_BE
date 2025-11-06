package com.kakaotechcampus.team16be.review.memberReview.repository;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.review.memberReview.domain.MemberReview;
import com.kakaotechcampus.team16be.user.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberReviewRepository extends JpaRepository<MemberReview, Long> {
    List<MemberReview> findByRevieweeAndGroup(User reviewee, Group group);

    List<MemberReview> findByreviewee(User reviewee);

    @Modifying
    @Query("DELETE FROM MemberReview mr WHERE mr.reviewer.id = :userId OR mr.reviewee.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);
}
