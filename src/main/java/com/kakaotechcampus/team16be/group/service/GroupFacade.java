package com.kakaotechcampus.team16be.group.service; // 편의상 같은 패키지에 생성

import com.kakaotechcampus.team16be.aws.service.S3UploadPresignedUrlService;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.dto.ResponseGroupListDto;
import com.kakaotechcampus.team16be.group.dto.ResponseSingleGroupDto;
import java.util.List;

import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.groupMember.service.GroupMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupFacade {

    private final GroupService groupService;
    private final S3UploadPresignedUrlService s3UploadPresignedUrlService;
    private final GroupMemberService groupMemberService;

    @Transactional(readOnly = true)
    public List<ResponseGroupListDto> getGroups() {
        List<Group> findGroups = groupService.getAllGroups();


        return findGroups.stream()
                .map(group -> {
                    String fullUrl = s3UploadPresignedUrlService.getGroupPublicUrl(group.getCoverImageUrl());
                    return ResponseGroupListDto.from(group, fullUrl);
                }).toList();
    }

    @Transactional(readOnly = true)
    public ResponseSingleGroupDto getGroup(Long groupId) {
        Group targetGroup = groupService.findGroupById(groupId);
        String fullUrl = s3UploadPresignedUrlService.getGroupPublicUrl(targetGroup.getCoverImageUrl());
        List<GroupMember> activeMember = groupMemberService.getActiveMember(targetGroup);

        return ResponseSingleGroupDto.from(targetGroup, fullUrl,activeMember.size());
    }
}
