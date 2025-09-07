package com.kakaotechcampus.team16be.aws.controller;

import com.kakaotechcampus.team16be.aws.dto.ImageUrlDto;
import com.kakaotechcampus.team16be.aws.dto.IssuePresignedUrlRequest;
import com.kakaotechcampus.team16be.aws.service.S3UploadPresignedUrlService;
import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

    private final S3UploadPresignedUrlService s3UploadPresignedUrlService;

    @PostMapping("/presigned")
    public ResponseEntity<ImageUrlDto> createPresignedUrl(
            @LoginUser Long userId,
            @RequestBody IssuePresignedUrlRequest request
    ) {
        ImageUrlDto imageUrlDto = s3UploadPresignedUrlService.execute(
                userId, request.fileExtension(), request.type());
        return ResponseEntity.ok(imageUrlDto);
    }

}
