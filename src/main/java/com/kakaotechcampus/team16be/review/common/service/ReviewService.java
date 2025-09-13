package com.kakaotechcampus.team16be.review.common.service;

import com.kakaotechcampus.team16be.review.common.dto.ReviewCreateDto;
import com.kakaotechcampus.team16be.user.domain.User;

import java.util.List;

public interface ReviewService<ReviewCreateReq extends ReviewCreateDto, Review> {
    Review createReview(User user, ReviewCreateReq createReviewDto);

    List<Review> getAllReviews(User user,Long groupId);
}