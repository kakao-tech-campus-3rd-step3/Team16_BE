package com.kakaotechcampus.team16be.aws.dto;

import com.kakaotechcampus.team16be.aws.domain.ImageFileExtension;
import com.kakaotechcampus.team16be.aws.domain.ImageUploadType;

public record IssuePresignedUrlRequest(
        ImageFileExtension fileExtension, //확장자
        ImageUploadType type //업로드 타입
) {
}
