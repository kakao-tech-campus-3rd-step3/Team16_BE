package com.kakaotechcampus.team16be.review.groupReview.repository;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.review.groupReview.domain.GroupReview;
import com.kakaotechcampus.team16be.user.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupReviewRepository extends JpaRepository<GroupReview, Integer> {

    List<GroupReview> findAllByUser(User user);

    List<GroupReview> findAllByGroup(Group group);
}
