package com.kakaotechcampus.team16be.groupMember.service;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.groupMember.exception.GroupMemberException;
import com.kakaotechcampus.team16be.groupMember.repository.GroupMemberRepository;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.kakaotechcampus.team16be.groupMember.domain.GroupMemberStatus.*;
import static com.kakaotechcampus.team16be.groupMember.exception.GroupMemberErrorCode.*;


@Service
@RequiredArgsConstructor
public class GroupMemberServiceImpl implements GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;
    private final GroupService groupService;
    private final UserService userService;

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

    @Transactional(readOnly = true)
    public List<GroupMember> findByGroupAndPendingUser(User user, Long groupId) {
        Group targetGroup = groupService.findGroupById(groupId);
        targetGroup.checkLeader(user);

        return groupMemberRepository.findAllByGroupAndStatus(targetGroup, PENDING);
    }

    @Override
    public List<GroupMember> getGroupMember(User user, Long groupId) {
        validateGroupMember(user, groupId);
        Group targetGroup = groupService.findGroupById(groupId);

        return groupMemberRepository.findAllByGroup(targetGroup);
    }
}