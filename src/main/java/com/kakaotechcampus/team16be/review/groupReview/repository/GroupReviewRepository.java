package com.kakaotechcampus.team16be.review.groupReview.repository;

import com.kakaotechcampus.team16be.review.groupReview.domain.GroupReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupReviewRepository extends JpaRepository<GroupReview,Integer> {

}
