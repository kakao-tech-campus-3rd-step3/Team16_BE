package com.kakaotechcampus.team16be.groupMember.service;

import static com.kakaotechcampus.team16be.groupMember.domain.GroupMemberStatus.ACTIVE;
import static com.kakaotechcampus.team16be.groupMember.domain.GroupMemberStatus.LEFT;
import static com.kakaotechcampus.team16be.groupMember.domain.GroupMemberStatus.PENDING;
import static com.kakaotechcampus.team16be.groupMember.exception.GroupMemberErrorCode.GROUP_MEMBER_NOT_FOUND;

import com.kakaotechcampus.team16be.aws.service.S3UploadPresignedUrlService;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.groupMember.dto.GroupMemberDto;
import com.kakaotechcampus.team16be.groupMember.dto.SignResponseDto;
import com.kakaotechcampus.team16be.groupMember.exception.GroupMemberException;
import com.kakaotechcampus.team16be.groupMember.repository.GroupMemberRepository;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class GroupMemberServiceImpl implements GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;
    private final GroupService groupService;
    private final UserService userService;
    private final S3UploadPresignedUrlService s3UploadPresignedUrlService;

    public GroupMember findByGroupAndUser(Group group, User user) {
        return groupMemberRepository.findByGroupAndUser(group, user)
                .orElseThrow(() -> new GroupMemberException(GROUP_MEMBER_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkMemberHasLeft(Group targetGroup, User user) {

        return groupMemberRepository.findByGroupAndUser(targetGroup, user)
                .map(member -> member.getStatus() == LEFT)
                .orElse(false);
    }

    @Override
    @Transactional
    public void createGroup(Group createdGroup, User user) {

        GroupMember groupMember = GroupMember.create(createdGroup, user);
        groupMemberRepository.save(groupMember);
    }


    @Override
    public List<GroupMember> findByGroup(Group targetGroup) {
        return groupMemberRepository.findAllByGroup((targetGroup));
    }

    @Override
    @Transactional
    public GroupMember cancelSignGroup(User user, Long groupId) {
        Group group = groupService.findGroupById(groupId);

        GroupMember groupMember = findByGroupAndUser(group, user);
        groupMember.canCancel();

        return groupMember;
    }


    @Override
    @Transactional
    public void changeLeader(Long groupId, User oldLeader, Long newLeaderId) {

        Group targetGroup = groupService.findGroupById(groupId);
        User newLeader = userService.findById(newLeaderId);

        GroupMember oldLeaderMember = findByGroupAndUser(targetGroup, oldLeader);
        GroupMember newLeaderMember = findByGroupAndUser(targetGroup, newLeader);

        targetGroup.checkLeader(oldLeader);
        oldLeaderMember.changeToMember();
        newLeaderMember.changeToLeader();

        targetGroup.changeLeader(newLeader);
    }

    public void validateGroupMember(User user, Long groupId) {
        Group targetGroup = groupService.findGroupById(groupId);
        GroupMember member = findByGroupAndUser(targetGroup, user);
        member.checkUserIsActive();

    }


    @Override
    @Transactional(readOnly = true)
    public List<SignResponseDto> findByGroupAndPendingUser(User user, Long groupId) {
        Group targetGroup = groupService.findGroupById(groupId);
        targetGroup.checkLeader(user);

        List<GroupMember> members = groupMemberRepository.findAllByGroupAndStatus(targetGroup, PENDING);

        return members.stream()
                .map(member -> {
                    User targetMember = member.getUser();
                    String fileName = targetMember.getProfileImageUrl();

                    String publicUrl = s3UploadPresignedUrlService.getGroupPublicUrl(fileName);

                    return new SignResponseDto(
                            targetMember.getId(),
                            member.getIntro(),
                            publicUrl
                    );
                })
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<GroupMemberDto> getGroupMember(User user, Long groupId) {
        Group targetGroup = groupService.findGroupById(groupId);
        List<GroupMember> members = groupMemberRepository.findAllByGroupAndStatus(targetGroup, ACTIVE);

        return members.stream()
                .map(member -> {
                    User memberUser = member.getUser();
                    String originalUrl = memberUser.getProfileImageUrl();
                    String publicUrl;
                    if (originalUrl == null || originalUrl.isEmpty()) {
                        publicUrl = s3UploadPresignedUrlService.getUserPublicUrl("");
                    } else {
                        publicUrl = s3UploadPresignedUrlService.getUserPublicUrl(originalUrl);
                    }

                    return new GroupMemberDto(
                            member.getId(),
                            member.getGroup().getName(),
                            memberUser.getId(),
                            memberUser.getNickname(),
                            member.getRole(),
                            publicUrl
                    );
                })
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<GroupMember> getActiveMember(Group group) {
        return groupMemberRepository.findAllByGroupAndStatus(group, ACTIVE);
    }
}
