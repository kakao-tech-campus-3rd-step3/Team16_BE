package com.kakaotechcampus.team16be.aws.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/***
 * 도메인별 이미지 분류
 *
 * 사용자 프로필 사진
 * 그룹 아이콘
 * 게시글 사진
 * 학생 인증 서류
 *
 */
@Getter
@RequiredArgsConstructor
public enum ImageUploadType {

    PROFILE("profile"),
    GROUP_ICON("group-icon"),
    POST("post"),
    VERIFICATION("verification");

    private final String type;
}