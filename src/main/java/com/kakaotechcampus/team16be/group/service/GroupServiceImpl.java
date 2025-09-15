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
import com.kakaotechcampus.team16be.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    /***
     * 추후 UserService 구현 후 추가 예정
     */
    //private final UserService userService;
    private final S3UploadPresignedUrlService s3UploadPresignedUrlService;


    @Transactional
    @Override
    public Group createGroup(Long userId, CreateGroupDto createGroupDto) {
        String groupName = createGroupDto.name();
        String groupIntro = createGroupDto.intro();
        Integer groupCapacity = createGroupDto.capacity();

        /***
         * 추후 추가 예정
         */
        // User user = userService.findById(userId);

        //임시 User 추가
        User user = new User("id");
        Group createdGroup = Group.createGroup(user, groupName, groupIntro, groupCapacity);

        if (existGroupName(createdGroup.getName())) {
            throw new GroupException(GroupErrorCode.GROUP_NAME_DUPLICATE);
        }

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
                    String fullUrl = s3UploadPresignedUrlService.getPublicUrl(group.getCoverImageUrl());
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
    public Group updateGroup(Long userId, Long groupId, UpdateGroupDto updateGroupDto) {
        Group targetGroup = findGroupById(groupId);
        String oldImgUrl = targetGroup.getCoverImageUrl();

        /***
         * User user = userService.findById(userId);
         */
        User user = new User("id"); // 임시 User 추가

        targetGroup.checkLeader(user);

        String updatedName = updateGroupDto.name();
        String updatedIntro = updateGroupDto.intro();
        Integer updatedCapacity = updateGroupDto.capacity();
        targetGroup.update(updatedName, updatedIntro, updatedCapacity);

        String updatedImgUrl = updateGroupDto.coverImageUrl();
        targetGroup.changeCoverImage(updatedImgUrl);

        boolean isImageChanged = !Objects.equals(updatedImgUrl, oldImgUrl);
        boolean isOldImageDefault = (oldImgUrl == null || oldImgUrl.equals(targetGroup.returnDefaultImgUrl()));

        if (isImageChanged && !isOldImageDefault) {
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

        String fullUrl = s3UploadPresignedUrlService.getPublicUrl(targetGroup.getCoverImageUrl());

        return ResponseSingleGroupDto.from(targetGroup, fullUrl);

    }

    public boolean existGroupName(String groupName) {
        return groupRepository.existsGroupByName(groupName);
    }

}
