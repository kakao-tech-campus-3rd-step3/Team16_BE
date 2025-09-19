package com.kakaotechcampus.team16be.auth.dto;

import com.kakaotechcampus.team16be.aws.domain.ImageUploadType;

public record UpdateStudentIdImageRequest(
        String fileName
) {
}
