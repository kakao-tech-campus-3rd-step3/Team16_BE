package com.kakaotechcampus.team16be.review.groupReview.service;

import com.kakaotechcampus.team16be.review.groupReview.domain.GroupReview;
import com.kakaotechcampus.team16be.review.groupReview.dto.CreateGroupReviewDto;
import com.kakaotechcampus.team16be.user.domain.User;

import java.util.List;

public interface GroupReviewService {

    GroupReview createReview(User user, CreateGroupReviewDto createGroupReviewDto);

    List<GroupReview> getAllReviews(User user, Long groupId);

}
