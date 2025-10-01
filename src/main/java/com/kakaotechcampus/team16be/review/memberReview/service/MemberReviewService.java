package com.kakaotechcampus.team16be.review.memberReview.service;

import com.kakaotechcampus.team16be.review.memberReview.domain.MemberReview;
import com.kakaotechcampus.team16be.review.memberReview.dto.CreateMemberReviewDto;
import com.kakaotechcampus.team16be.user.domain.User;

import java.util.List;

public interface MemberReviewService {

    MemberReview createReview(User user, CreateMemberReviewDto createReviewDto);

    List<MemberReview> getAllReviewsByGroup(User user, Long groupId);

    List<MemberReview> getAllReviews(User user);
}
