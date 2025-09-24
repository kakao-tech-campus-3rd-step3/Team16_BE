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

import java.util.Optional;

import static com.kakaotechcampus.team16be.groupMember.domain.GroupMemberStatus.*;
import static com.kakaotechcampus.team16be.groupMember.exception.GroupMemberErrorCode.*;


@Service
@RequiredArgsConstructor
public class GroupMemberServiceImpl implements GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;
    private final GroupService groupService;
    private final UserService userService;


    @Transactional
    public GroupMember joinGroup(Long groupId, Long joinerId ,Long leaderId) throws GroupMemberException {
        Group group = groupService.findGroupById(groupId);

        User leader = userService.findById(leaderId);
        group.checkLeader(leader);

        User joiner = userService.findById(joinerId);

        Optional<GroupMember> existingMember = groupMemberRepository.findByGroupAndUser(group, joiner);

    if (existingMember.isPresent()) {
        GroupMember member = existingMember.get();
        member.acceptJoin();
        return member;
    }
    else {
        GroupMember newMember = GroupMember.acceptJoin(group, joiner);
        return groupMemberRepository.save(newMember);
    }

    }
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
    @Transactional
    public GroupMember signGroup(User user, Long groupId) {
        User signedUser = userService.findById(user.getId());
        Group targetGroup = groupService.findGroupById(groupId);

         GroupMember signMember = GroupMember.sign(signedUser, targetGroup);

        return groupMemberRepository.save(signMember);
    }

    @Override
    @Transactional
    public GroupMember cancelSignGroup(User user, Long groupId) {
        Group group = groupService.findGroupById(groupId);

        GroupMember groupMember = findByGroupAndUser(group, user);
        groupMember.cancelSignGroup();

        return groupMember;
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