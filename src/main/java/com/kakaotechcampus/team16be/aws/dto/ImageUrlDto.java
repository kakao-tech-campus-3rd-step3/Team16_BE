package com.kakaotechcampus.team16be.aws.dto;

public record ImageUrlDto(
        String url, //presigned-URL, 클라이언트가 이 URL로 직접 S3에 업로드 가능
        String fileName //S3 상의 실제 저장 경로, 서버가 나중에 삭제하거나 관리할 때 필요
) {
    public static ImageUrlDto of(String url, String fileName) {
        return new ImageUrlDto(url, fileName);
    }
}
