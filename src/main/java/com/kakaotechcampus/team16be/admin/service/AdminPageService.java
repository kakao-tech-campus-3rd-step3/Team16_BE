package com.kakaotechcampus.team16be.admin.service;

import com.kakaotechcampus.team16be.admin.dto.AdminUserVerificationView;
import com.kakaotechcampus.team16be.aws.service.S3UploadPresignedUrlService;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.domain.VerificationStatus;
import com.kakaotechcampus.team16be.user.exception.UserErrorCode;
import com.kakaotechcampus.team16be.user.exception.UserException;
import com.kakaotechcampus.team16be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminPageService {

    private final UserRepository userRepository;
    private final S3UploadPresignedUrlService s3Service;

    @Transactional(readOnly = true)
    public List<AdminUserVerificationView> getAllVerificationRequests() {
        return userRepository.findAll().stream()
                .map(user -> new AdminUserVerificationView(
                        user.getId(),
                        user.getNickname(),
                        s3Service.getPublicUrl(user.getStudentIdImageUrl()),
                        user.getVerificationStatus().name()
                ))
                .toList();
    }

    @Transactional
    public void updateVerificationStatus(Long userId, String status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        user.updateVerificationStatus(VerificationStatus.valueOf(status));
    }

}
