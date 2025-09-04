package com.kakaotechcampus.team16be.group.service;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.dto.CreateGroupDto;
import com.kakaotechcampus.team16be.group.dto.UpdateGroupDto;
import com.kakaotechcampus.team16be.group.dto.UploadGroupImageDto;
import com.kakaotechcampus.team16be.group.exception.ErrorCode;
import com.kakaotechcampus.team16be.group.exception.GroupException;
import com.kakaotechcampus.team16be.group.repository.GroupRepository;
import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    @Value("${upload.path}")
    private String uploadPath;

    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
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

        String updatedName = updateGroupDto.name();
        String updatedIntro = updateGroupDto.intro();
        Integer updatedCapacity = updateGroupDto.capacity();

        return targetGroup.update(updatedName, updatedIntro, updatedCapacity);

    }

    @Transactional
    @Override
    public void uploadGroupImage(Long groupId, UploadGroupImageDto uploadGroupImageDto) throws IOException {
        Group targetGroup = findGroupById(groupId);

        MultipartFile imageFile = uploadGroupImageDto.multipartFile();

        if (imageFile.isEmpty()) {
            throw new GroupException(ErrorCode.GROUP_IMAGE_ISNULL);
        }

        if (!targetGroup.getCoverImageUrl().isEmpty()) {
            deleteGroupImage(targetGroup.getCoverImageUrl());
        }

        String originalImageFilename = imageFile.getOriginalFilename();
        String storedImageFileName = UUID.randomUUID().toString() + "_" + originalImageFilename;
        String fullPath = uploadPath + storedImageFileName;

        imageFile.transferTo(new File(fullPath));

        String imageUrlForClient = "/images/" + storedImageFileName;
        targetGroup.setCoverImageUrl(imageUrlForClient);

    }

    private void deleteGroupImage(String coverImageUrl) {
        String imageName = coverImageUrl.substring("/images".length());
        File oldFile = new File(uploadPath + imageName);
        if (oldFile.exists()) {
            oldFile.delete();
        }
    }

    public Group findGroupById(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupException(ErrorCode.GROUP_CANNOT_FOUND));
    }

    public boolean existGroupName(String groupName) {
        return groupRepository.existsGroupByName(groupName);
    }



}
