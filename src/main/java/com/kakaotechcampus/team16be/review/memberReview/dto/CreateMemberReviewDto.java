package com.kakaotechcampus.team16be.review.memberReview.dto;

import com.kakaotechcampus.team16be.review.common.dto.BaseReviewCreateDto;
import com.kakaotechcampus.team16be.review.memberReview.domain.Evaluation;
import lombok.Getter;

@Getter
public class CreateMemberReviewDto extends BaseReviewCreateDto {
    private Long groupId;
    private Long revieweeId;
    private Evaluation evaluation;
}
