package com.kakaotechcampus.team16be.review.groupReview.service;

import com.kakaotechcampus.team16be.review.common.ReviewCreateDto;
import com.kakaotechcampus.team16be.review.groupReview.dto.CreateGroupReviewDto;
import com.kakaotechcampus.team16be.user.domain.User;

public interface ReviewService<ReviewCreateReq extends ReviewCreateDto, Review> {
    Review createReview(User user, ReviewCreateReq createReviewDto);
}