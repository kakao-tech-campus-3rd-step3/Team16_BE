package com.kakaotechcampus.team16be.group.service;

import com.kakaotechcampus.team16be.aws.service.S3UploadPresignedUrlService;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.dto.CreateGroupDto;
import com.kakaotechcampus.team16be.group.dto.ResponseGroupListDto;
import com.kakaotechcampus.team16be.group.dto.ResponseSingleGroupDto;
import com.kakaotechcampus.team16be.group.dto.UpdateGroupDto;
import com.kakaotechcampus.team16be.group.exception.GroupErrorCode;
import com.kakaotechcampus.team16be.group.exception.GroupException;
import com.kakaotechcampus.team16be.group.repository.GroupRepository;
import com.kakaotechcampus.team16be.groupMember.service.GroupMemberService;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.service.UserService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class GroupServiceImpl implements GroupService {

    @Value("${cloud.aws.s3.default-image-url}")
    private String defaultImageUrl;

    private final GroupRepository groupRepository;
    private final GroupMemberService groupMemberService;
    private final UserService userService;
    private final S3UploadPresignedUrlService s3UploadPresignedUrlService;

    public GroupServiceImpl(GroupRepository groupRepository, @Lazy GroupMemberService groupMemberService, UserService userService, S3UploadPresignedUrlService s3UploadPresignedUrlService) {
        this.groupRepository = groupRepository;
        this.groupMemberService = groupMemberService;
        this.userService = userService;
        this.s3UploadPresignedUrlService = s3UploadPresignedUrlService;
    }


    @Transactional
    @Override
    public Group createGroup(User user, CreateGroupDto createGroupDto) {
        String groupName = createGroupDto.name();
        String groupIntro = createGroupDto.intro();
        Integer groupCapacity = createGroupDto.capacity();

        Group createdGroup = Group.createGroup(user, groupName, groupIntro, groupCapacity);

        if (existGroupName(createdGroup.getName())) {
            throw new GroupException(GroupErrorCode.GROUP_NAME_DUPLICATE);
        }

        groupMemberService.createGroup(createdGroup, user);

        return groupRepository.save(createdGroup);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ResponseGroupListDto> getAllGroups() {

        List<Group> findGroups = groupRepository.findAll();

        if (findGroups.isEmpty()) {
            throw new GroupException(GroupErrorCode.GROUP_CANNOT_FOUND);
        }

        return findGroups.stream()
                .map(group -> {
                    String coverImageUrl = group.getCoverImageUrl();
                    String imageUrlToUse = (coverImageUrl == null || coverImageUrl.isEmpty()) ? defaultImageUrl : coverImageUrl;
                    String fullUrl = s3UploadPresignedUrlService.getPublicUrl(imageUrlToUse);
                    return ResponseGroupListDto.from(group, fullUrl);
                }).toList();
    }

    /***
     * 관리자전용 삭제 or 그룹장전용 삭제?
     */
    @Transactional
    @Override
    public void deleteGroup(Long groupId) {
        findGroupById(groupId);
        groupRepository.deleteById(groupId);
    }

    @Transactional
    @Override
    public Group updateGroup(User user, Long groupId, UpdateGroupDto updateGroupDto) {
        Group targetGroup = findGroupById(groupId);
        String oldImgUrl = targetGroup.getCoverImageUrl();

        targetGroup.checkLeader(user);

        String updatedName = updateGroupDto.name();
        String updatedIntro = updateGroupDto.intro();
        Integer updatedCapacity = updateGroupDto.capacity();
        targetGroup.update(updatedName, updatedIntro, updatedCapacity);

        String updatedImgUrl = updateGroupDto.coverImageUrl();
        targetGroup.changeCoverImage(updatedImgUrl);

        boolean isImageChanged = !Objects.equals(updatedImgUrl, oldImgUrl);

        if (isImageChanged && oldImgUrl != null) {
            s3UploadPresignedUrlService.deleteImage(oldImgUrl);
        }
        return targetGroup;

    }

    @Override
    public Group findGroupById(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupException(GroupErrorCode.GROUP_CANNOT_FOUND));
    }

    @Override
    public ResponseSingleGroupDto getGroup(Long groupId) {
        Group targetGroup = findGroupById(groupId);
        String fullUrl;
        if (targetGroup.getCoverImageUrl().isEmpty()) {
            fullUrl = s3UploadPresignedUrlService.getPublicUrl(defaultImageUrl);
        }else {
            fullUrl = s3UploadPresignedUrlService.getPublicUrl(targetGroup.getCoverImageUrl());
        }
        return ResponseSingleGroupDto.from(targetGroup, fullUrl);

    }

    public boolean existGroupName(String groupName) {
        return groupRepository.existsGroupByName(groupName);
    }

}
