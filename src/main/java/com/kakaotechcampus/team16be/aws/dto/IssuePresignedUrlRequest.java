package com.kakaotechcampus.team16be.aws.dto;

import com.kakaotechcampus.team16be.aws.domain.ImageFileExtension;
import com.kakaotechcampus.team16be.aws.domain.ImageUploadType;

public record IssuePresignedUrlRequest(
        Long userId, //누가 업로드 하는지 식별용 (user의 기본키인 userId)
        ImageFileExtension fileExtension, //확장자
        ImageUploadType type //업로드 타입
) {}
