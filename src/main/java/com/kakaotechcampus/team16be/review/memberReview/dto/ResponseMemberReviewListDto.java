package com.kakaotechcampus.team16be.review.memberReview.dto;

import com.kakaotechcampus.team16be.review.memberReview.domain.Evaluation;
import com.kakaotechcampus.team16be.review.memberReview.domain.MemberReview;

import java.util.List;

public record ResponseMemberReviewListDto(
        Long groupId,
        String groupName,
        String content,
        Evaluation evaluation
) {
    public static ResponseMemberReviewListDto from(MemberReview memberReview) {

        return new ResponseMemberReviewListDto(
                memberReview.getGroup().getId(),
                memberReview.getGroup().getName(),
                memberReview.getContent(),
                memberReview.getEvaluation()
        );
    }

    public static List<ResponseMemberReviewListDto> from(List<MemberReview> memberReviews) {
        return memberReviews.stream()
                .map(ResponseMemberReviewListDto::from)
                .toList();
    }
}