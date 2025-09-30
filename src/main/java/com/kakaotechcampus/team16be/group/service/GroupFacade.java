package com.kakaotechcampus.team16be.group.service; // 편의상 같은 패키지에 생성

import com.kakaotechcampus.team16be.aws.service.S3UploadPresignedUrlService;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.dto.ResponseGroupListDto;
import com.kakaotechcampus.team16be.group.dto.ResponseSingleGroupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupFacade {

    private final GroupService groupService;
    private final S3UploadPresignedUrlService s3UploadPresignedUrlService;

    @Transactional(readOnly = true)
    public List<ResponseGroupListDto> getGroups() {
        List<Group> findGroups = groupService.getAllGroups();

        return findGroups.stream()
                .map(group -> {
                    String fullUrl = s3UploadPresignedUrlService.getPublicUrl(group.getCoverImageUrl());
                    return ResponseGroupListDto.from(group, fullUrl);
                }).toList();
    }

    @Transactional(readOnly = true)
    public ResponseSingleGroupDto getGroup(Long groupId) {
        Group targetGroup = groupService.findGroupById(groupId);
        String fullUrl = s3UploadPresignedUrlService.getPublicUrl(targetGroup.getCoverImageUrl());
        return ResponseSingleGroupDto.from(targetGroup, fullUrl);
    }
}