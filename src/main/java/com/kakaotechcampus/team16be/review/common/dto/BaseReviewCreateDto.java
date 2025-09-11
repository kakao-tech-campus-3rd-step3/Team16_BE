package com.kakaotechcampus.team16be.review.common.dto;

import lombok.Getter;

@Getter
public abstract class BaseReviewCreateDto implements ReviewCreateDto {
    private String content;
}
