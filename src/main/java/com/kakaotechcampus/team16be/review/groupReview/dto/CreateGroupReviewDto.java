package com.kakaotechcampus.team16be.review.groupReview.dto;


import com.kakaotechcampus.team16be.review.common.BaseReviewCreateDto;
import com.kakaotechcampus.team16be.review.common.ReviewCreateDto;
import lombok.Getter;

@Getter
public class CreateGroupReviewDto extends BaseReviewCreateDto {
    private Long groupId;
}