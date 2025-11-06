package com.kakaotechcampus.team16be.aws.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/***
 * 지원하는 이미지 파일 확장자 : jpg, png, heic
 */
@Getter
@RequiredArgsConstructor
public enum ImageFileExtension {

    JPG("jpg"),
    JPEG("jpeg"),
    PNG("png"),
    HEIC("heic"),
    HEIF("heif"),
    GIF("gif"),
    BMP("bmp"),
    TIFF("tiff"),
    TIF("tif"),
    WEBP("webp");

    private final String uploadExtension;
}
