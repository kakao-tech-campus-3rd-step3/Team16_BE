package com.kakaotechcampus.team16be.review.memberReview.repository;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.review.memberReview.domain.MemberReview;
import com.kakaotechcampus.team16be.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberReviewRepository extends JpaRepository<MemberReview,Long> {
    List<MemberReview> findByRevieweeAndGroup(User reviewee, Group group);
}
