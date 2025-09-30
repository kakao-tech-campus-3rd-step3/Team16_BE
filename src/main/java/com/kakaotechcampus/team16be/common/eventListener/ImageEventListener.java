package com.kakaotechcampus.team16be.common.eventListener;

import com.kakaotechcampus.team16be.aws.service.S3UploadPresignedUrlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageEventListener {

    private final S3UploadPresignedUrlService s3UploadPresignedUrlService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onImageDeleted(ImageDeletedEvent event) {
        s3UploadPresignedUrlService.deleteImage(event.fileName());
    }
}