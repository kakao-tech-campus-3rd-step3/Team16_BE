package com.kakaotechcampus.team16be.user.service;

import com.kakaotechcampus.team16be.auth.dto.StudentVerificationStatusResponse;
import com.kakaotechcampus.team16be.auth.dto.UpdateStudentIdImageRequest;
import com.kakaotechcampus.team16be.aws.service.S3UploadPresignedUrlService;
import com.kakaotechcampus.team16be.group.repository.GroupRepository;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMemberStatus;
import com.kakaotechcampus.team16be.groupMember.repository.GroupMemberRepository;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.dto.UserGroupHistoryResponse;
import com.kakaotechcampus.team16be.user.dto.UserInfoResponse;
import com.kakaotechcampus.team16be.user.dto.UserNicknameRequest;
import com.kakaotechcampus.team16be.user.dto.UserNicknameResponse;
import com.kakaotechcampus.team16be.user.exception.UserErrorCode;
import com.kakaotechcampus.team16be.user.exception.UserException;
import com.kakaotechcampus.team16be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final S3UploadPresignedUrlService s3UploadPresignedUrlService;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;

    @Transactional
    public void updateStudentIdImage(Long userId, UpdateStudentIdImageRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        user.updateStudentIdImageUrl(request.fileName());
        user.updateVerificationStatusPending();

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public StudentVerificationStatusResponse getVerificationStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        return StudentVerificationStatusResponse.of(
                true,
                user.getVerificationStatus(),
                null
        );
    }

    @Transactional(readOnly = true)
    public String getStudentIdImageUrl(User user) {
        String fileName = user.getStudentIdImageUrl();
        return s3UploadPresignedUrlService.getSecureUrl(fileName);
    }

    @Transactional
    public void createProfileImage(Long userId, String fileName) {
        User user = getUser(userId);
        if (user.getProfileImageUrl() != null) {
            throw new UserException(UserErrorCode.PROFILE_IMAGE_ALREADY_EXISTS);
        }
        user.updateProfileImageUrl(fileName);
        userRepository.save(user);
    }

    @Transactional
    public void updateProfileImage(Long userId, String fileName) {
        User user = getUser(userId);
        user.updateProfileImageUrl(fileName);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public String getProfileImage(Long userId) {
        User user = getUser(userId);
        String profileImageUrl = user.getProfileImageUrl();
        if (profileImageUrl == null) {
            return null;
        }
        return s3UploadPresignedUrlService.getPublicUrl(profileImageUrl);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public void deleteProfileImage(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        user.updateProfileImageUrl(null);
    }

    @Transactional
    public void createNickname(User user, UserNicknameRequest request) {
        user.updateNickname(request.nickname());
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserNicknameResponse getNickname(User user) {
        return UserNicknameResponse.from(user.getNickname());
    }

    @Transactional
    public void updateNickname(User user, UserNicknameRequest request) {
        user.updateNickname(request.nickname());
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        List<String> leaderGroupIds = groupRepository.findLeaderGroupIdsByUserId(userId);
        List<String> memberGroupIds = groupRepository.findMemberGroupIdsByUserId(userId);

        Map<String, List<String>> groups = Map.of(
                "leaderOf", leaderGroupIds,
                "memberOf", memberGroupIds
        );

        String profileImageUrl = s3UploadPresignedUrlService.getPublicUrl(user.getProfileImageUrl());
        return UserInfoResponse.of(user, groups, profileImageUrl);
    }

    @Transactional(readOnly = true)
    public List<UserGroupHistoryResponse> getUserGroupHistory(Long userId) {
        List<GroupMember> memberships = groupMemberRepository.findAllByUserIdAndStatusIn(
                userId,
                List.of(GroupMemberStatus.ACTIVE, GroupMemberStatus.LEFT, GroupMemberStatus.BANNED)
        );

        return memberships.stream()
                .map(UserGroupHistoryResponse::from)
                .toList();
    }

    @Transactional
    public void increaseUserScoreByAttendance(User user) {
        user.increaseScoreByAttendance();
    }

    @Transactional
    public void decreaseScoreByAbsent(User user) {
        user.decreaseScoreByAbsent();
    }

    @Transactional
    public void increaseUserScoreByPosting(User user) {
        user.increaseScoreByPosting();
    }
}
