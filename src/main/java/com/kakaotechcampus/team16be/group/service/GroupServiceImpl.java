package com.kakaotechcampus.team16be.group.service;

import com.kakaotechcampus.team16be.aws.service.S3UploadPresignedUrlService;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.dto.CreateGroupDto;
import com.kakaotechcampus.team16be.group.dto.UpdateGroupDto;
import com.kakaotechcampus.team16be.group.exception.ErrorCode;
import com.kakaotechcampus.team16be.group.exception.GroupException;
import com.kakaotechcampus.team16be.group.repository.GroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final S3UploadPresignedUrlService s3UploadPresignedUrlService;

    public GroupServiceImpl(GroupRepository groupRepository, S3UploadPresignedUrlService s3UploadPresignedUrlService) {
        this.groupRepository = groupRepository;
        this.s3UploadPresignedUrlService = s3UploadPresignedUrlService;
    }

    @Transactional
    @Override
    public Group createGroup(CreateGroupDto createGroupDto) {
        String groupName = createGroupDto.name();
        String groupIntro = createGroupDto.intro();
        Integer groupCapacity = createGroupDto.capacity();

        Group createdGroup = new Group(groupName, groupIntro, groupCapacity);

        if (existGroupName(createdGroup.getName())) {
            throw new GroupException(ErrorCode.GROUP_NAME_DUPLICATE);
        }

        return groupRepository.save(createdGroup);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Group> getAllGroups() {

        List<Group> findGroups = groupRepository.findAll();

        if (findGroups.isEmpty()) {
            throw new GroupException(ErrorCode.GROUP_CANNOT_FOUND);
        }

        return findGroups;
    }

    @Transactional
    @Override
    public void deleteGroup(Long groupId) {
        findGroupById(groupId);
        groupRepository.deleteById(groupId);
    }

    @Transactional
    @Override
    public Group updateGroup(Long groupId, UpdateGroupDto updateGroupDto) {
        Group targetGroup = findGroupById(groupId);
        String oldImgUrl = targetGroup.getCoverImageUrl();

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
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupException(ErrorCode.GROUP_CANNOT_FOUND));
    }

    public boolean existGroupName(String groupName) {
        return groupRepository.existsGroupByName(groupName);
    }

}
