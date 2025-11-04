package com.kakaotechcampus.team16be.aws.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.kakaotechcampus.team16be.aws.domain.ImageFileExtension;
import com.kakaotechcampus.team16be.aws.domain.ImageUploadType;
import com.kakaotechcampus.team16be.aws.dto.ImageUrlDto;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3UploadPresignedUrlService {

    @Value("${cloud.aws.s3.default-image-url}")
    private String defaultCoverImageUrl;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;
    private final GroupService groupService;

    public ImageUrlDto execute(
            Long userId, ImageFileExtension fileExtension, ImageUploadType type
    ) {
        String valueFileExtension = fileExtension.getUploadExtension();
        String valueType = type.getType();
        String fileName = createFileName(userId, valueFileExtension, valueType);
        log.info(fileName);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                getGeneratePreSignedUrlRequest(bucket, fileName, valueFileExtension);
        URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);

        return ImageUrlDto.of(url.toString(), fileName);
    }

    private String createFileName(Long userId, String fileExtension, String valueType) {
        return valueType + "/" + userId + "/" + UUID.randomUUID() + "." + fileExtension;
    }

    // 업로드용 Pre-Signed URL을 생성하기 때문에, PUT을 지정
    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(
            String bucket, String fileName, String fileExtension
    ) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, fileName)
                        .withMethod(HttpMethod.PUT)
                        .withKey(fileName)
                        .withContentType("image/" + fileExtension)
                        .withExpiration(getPreSignedUrlExpiration());
//        generatePresignedUrlRequest.addRequestParameter(
//                Headers.S3_CANNED_ACL, CannedAccessControlList.PublicRead.toString());
        return generatePresignedUrlRequest;
    }

    // 유효 기간 설정 (1분)
    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 60 * 1000;
        expiration.setTime(expTimeMillis);
        return expiration;
    }

    public void deleteImage(String fileName) {
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, fileName);
        amazonS3Client.deleteObject(deleteObjectRequest);
    }

    public String getPublicUrl(String fileName) {
        if (fileName == null) {
            return null;
        } else if (fileName.isEmpty()) {
            return amazonS3Client.getUrl(bucket, defaultCoverImageUrl).toString();
        }
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    public String getSecureUrl(String fileName) {
        if (fileName == null) {
            return amazonS3Client.getUrl(bucket, defaultCoverImageUrl).toString();
        }

        Date expiration = new Date();
        long expTimeMillis = expiration.getTime() + 1000 * 60 * 60; // URL 유효 시간: 1시간
        expiration.setTime(expTimeMillis);

        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, fileName)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);

        return amazonS3Client.generatePresignedUrl(request).toString();
    }

    public ImageUrlDto executeGroupImg(User user, ImageFileExtension fileExtension) {
        String groupUUID = UUID.randomUUID().toString();
        String valueFileExtension = fileExtension.getUploadExtension();
        String valueType = String.valueOf(ImageUploadType.GROUP_ICON);
        String fileName = createGroupFileName(groupUUID, valueFileExtension, valueType);
        log.info(fileName);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                getGeneratePreSignedUrlRequest(bucket, fileName, valueFileExtension);
        URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);

        return ImageUrlDto.of(url.toString(), fileName);
    }

    private String createGroupFileName(String groupUUID, String fileExtension, String valueType) {
        return valueType + "/" + groupUUID + "/" + UUID.randomUUID() + "." + fileExtension;
    }

    public ImageUrlDto executePostImg(User user, Long groupId, ImageFileExtension fileExtension) {
        Group targetGroup = groupService.findGroupById(groupId);
        targetGroup.checkLeader(user);

        String postUUID = UUID.randomUUID().toString();
        System.out.println(postUUID);
        String valueFileExtension = fileExtension.getUploadExtension();
        String valueType = String.valueOf(ImageUploadType.POST);
        String fileName = createPostFileName(groupId, postUUID, valueFileExtension, valueType);
        log.info(fileName);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                getGeneratePreSignedUrlRequest(bucket, fileName, valueFileExtension);
        URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);

        return ImageUrlDto.of(url.toString(), fileName);
    }

    private String createPostFileName(Long groupId, String postUUID, String fileExtension,String valueType) {
        return valueType + "/" + groupId + "/" + postUUID + "/" + UUID.randomUUID() + "." + fileExtension;
    }
}