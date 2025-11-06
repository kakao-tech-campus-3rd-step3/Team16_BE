package com.kakaotechcampus.team16be.review.groupReview.dto;


import com.kakaotechcampus.team16be.review.common.dto.BaseReviewCreateDto;
import lombok.Getter;

@Getter
public class CreateGroupReviewDto extends BaseReviewCreateDto {
    private Long groupId;
}
