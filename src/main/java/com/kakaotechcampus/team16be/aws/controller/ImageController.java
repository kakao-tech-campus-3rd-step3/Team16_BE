package com.kakaotechcampus.team16be.aws.controller;

import com.kakaotechcampus.team16be.aws.dto.ImageUrlDto;
import com.kakaotechcampus.team16be.aws.dto.IssuePresignedUrlRequest;
import com.kakaotechcampus.team16be.aws.service.S3UploadPresignedUrlService;
import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
@Tag(name = "이미지 공통 API", description = "이미지 관련 공통 API")
public class ImageController {

    private final S3UploadPresignedUrlService s3UploadPresignedUrlService;

    @Operation(summary = "S3 이미지 주소 요청", description = "이미지 업로드할 presigned-url을 반환합니다.")
    @PostMapping("/presigned")
    public ResponseEntity<ImageUrlDto> createPresignedUrl(
            @LoginUser User user,
            @RequestBody IssuePresignedUrlRequest request
    ) {
        ImageUrlDto imageUrlDto = s3UploadPresignedUrlService.execute(
                user.getId(), request.fileExtension(), request.type());
        return ResponseEntity.ok(imageUrlDto);
    }

    @Operation(summary = "S3 그룹 이미지 주소 요청", description = "이미지 업로드할 그룹 이미지 presigned-url을 반환합니다.")
    @PostMapping("/presigned/groups")
    public ResponseEntity<ImageUrlDto> createGroupIconPresignedUrl(
            @LoginUser User user,
            @RequestBody IssuePresignedUrlRequest request
    ) {
        ImageUrlDto imageUrlDto = s3UploadPresignedUrlService.executeGroupImg(
                user, request.fileExtension());
        return ResponseEntity.ok(imageUrlDto);
    }

    @Operation(summary = "S3 게시글 이미지 주소 요청", description = "게시글 이미지 업로드할 presigned-url을 반환합니다.")
    @PostMapping("/presigned/group/{groupId}/posts")
    public ResponseEntity<ImageUrlDto> createPostImgPresignedUrls(
            @LoginUser User user,
            @PathVariable Long groupId,
            @RequestBody IssuePresignedUrlRequest request
    ) {

        ImageUrlDto imageURlDto = s3UploadPresignedUrlService.executePostImg(
                user,
                groupId,
                request.fileExtension()
        );
        return ResponseEntity.ok(imageURlDto);
    }


}
