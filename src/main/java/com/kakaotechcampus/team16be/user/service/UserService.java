package com.kakaotechcampus.team16be.user.service;

import com.kakaotechcampus.team16be.auth.dto.UpdateStudentIdImageRequest;
import com.kakaotechcampus.team16be.aws.domain.ImageUploadType;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.exception.UserErrorCode;
import com.kakaotechcampus.team16be.user.exception.UserException;
import com.kakaotechcampus.team16be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void updateStudentIdImage(Long userId, UpdateStudentIdImageRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        if (request.type() == ImageUploadType.VERIFICATION) {
            user.updateStudentIdImageUrl(request.fileName());
        }

        userRepository.save(user);
    }
}
