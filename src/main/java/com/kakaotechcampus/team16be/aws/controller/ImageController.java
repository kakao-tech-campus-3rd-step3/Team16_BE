package com.kakaotechcampus.team16be.aws.controller;

import com.kakaotechcampus.team16be.aws.dto.ImageUrlDto;
import com.kakaotechcampus.team16be.aws.dto.IssuePresignedUrlRequest;
import com.kakaotechcampus.team16be.aws.service.S3UploadPresignedUrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image")
public class ImageController {

    private final S3UploadPresignedUrlService s3UploadPresignedUrlService;

    public ImageController(S3UploadPresignedUrlService s3UploadPresignedUrlService) {
        this.s3UploadPresignedUrlService = s3UploadPresignedUrlService;
    }

    @PostMapping("/presigned")
    public ResponseEntity<ImageUrlDto> createPresignedUrl(@RequestBody IssuePresignedUrlRequest request) {
        ImageUrlDto imageUrlDto = s3UploadPresignedUrlService.execute(
                request.userId(), request.fileExtension(), request.type());
        return ResponseEntity.ok(imageUrlDto);
    }

}
