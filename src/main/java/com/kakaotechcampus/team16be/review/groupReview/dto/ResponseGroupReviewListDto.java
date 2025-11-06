package com.kakaotechcampus.team16be.review.groupReview.dto;

import com.kakaotechcampus.team16be.review.groupReview.domain.GroupReview;
import java.util.List;

public record ResponseGroupReviewListDto(String contents) {

    public static ResponseGroupReviewListDto from(GroupReview groupReview) {
        return new ResponseGroupReviewListDto(
                groupReview.getContent());

    }

    public static List<ResponseGroupReviewListDto> from(List<GroupReview> groupReviews) {
        return groupReviews.stream()
                .map(ResponseGroupReviewListDto::from)
                .toList();
    }
}
