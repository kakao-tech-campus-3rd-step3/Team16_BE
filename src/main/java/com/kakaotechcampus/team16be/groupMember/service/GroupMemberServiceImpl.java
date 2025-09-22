package com.kakaotechcampus.team16be.groupMember.service;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMemberStatus;
import com.kakaotechcampus.team16be.groupMember.exception.GroupMemberErrorCode;
import com.kakaotechcampus.team16be.groupMember.exception.GroupMemberException;
import com.kakaotechcampus.team16be.groupMember.repository.GroupMemberRepository;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class GroupMemberServiceImpl implements GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;
    private final GroupService groupService;
    private final UserService userService;


    @Transactional
    public GroupMember joinGroup(Long groupId, Long joinerId ,Long leaderId) {
        Group group = groupService.findGroupById(groupId);

        User user = userService.findById(leaderId);
        checkGroupLeader(group,user.getId());

        User joiner = userService.findById(joinerId);

        Optional<GroupMember> existingMember = groupMemberRepository.findByGroupAndUser(group, joiner);

        if (existingMember.isPresent()) {
            GroupMember member = existingMember.get();
            member.join();
            return member;
        } else {
            GroupMember newMember = GroupMember.join(group, joiner);
            return groupMemberRepository.save(newMember);
        }

    }

    public GroupMember findByGroupAndUser(Group group, User user) {
        return groupMemberRepository.findByGroupAndUser(group, user)
                .orElseThrow(() -> new GroupMemberException(GroupMemberErrorCode.GROUP_MEMBER_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkMemberHasLeft(Group targetGroup, User user) {

        return groupMemberRepository.findByGroupAndUser(targetGroup, user)
                .map(member -> member.getStatus() == GroupMemberStatus.LEFT)
                .orElse(false);
    }

    @Override
    public void createGroup(Group createdGroup, User user) {

        GroupMember groupMember = GroupMember.create(createdGroup, user);
        groupMemberRepository.save(groupMember);
    }

    @Override
    public GroupMember signGroup(User user, Long groupId) {
        User signedUser = userService.findById(user.getId());
        Group targetGroup = groupService.findGroupById(groupId);

         GroupMember signMember = GroupMember.sign(signedUser, targetGroup);

        return groupMemberRepository.save(signMember);
    }

    @Override
    public List<GroupMember> findByGroup(Group targetGroup) {
        return groupMemberRepository.findAllByGroup((targetGroup));
    }


    private void checkGroupLeader(Group group, Long userId) {
        if (!group.getLeader().getId().equals(userId)) {
            throw new GroupMemberException(GroupMemberErrorCode.LEADER_CANNOT_JOIN);
        }
    }


    @Transactional
    public GroupMember leaveGroup(Long groupId, Long userId) {
        Group group = groupService.findGroupById(groupId);

        User user = userService.findById(userId);

        GroupMember member = findByGroupAndUser(group, user);

        GroupMember.checkLeftGroup(member);


        member.leaveGroup();

        return member;
    }

    @Transactional
    public GroupMember bannedGroup(Long groupId, Long userId, User leaderUser) {
        Group group = groupService.findGroupById(groupId);

        group.checkLeader(leaderUser);

        User bannedUser = userService.findById(userId);

        GroupMember member = findByGroupAndUser(group, bannedUser);

        member.bannedGroup();

        return member;

    }
}