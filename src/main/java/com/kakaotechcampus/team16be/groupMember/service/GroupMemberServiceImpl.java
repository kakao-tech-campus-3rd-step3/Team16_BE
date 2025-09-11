package com.kakaotechcampus.team16be.groupMember.service;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.groupMember.exception.ErrorCode;
import com.kakaotechcampus.team16be.groupMember.exception.GroupMemberException;
import com.kakaotechcampus.team16be.groupMember.repository.GroupMemberRepository;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.exception.UserErrorCode;
import com.kakaotechcampus.team16be.user.exception.UserException;
import com.kakaotechcampus.team16be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class GroupMemberServiceImpl implements GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;
    private final GroupService groupService;
    private final UserRepository userRepository;

    @Transactional
    public GroupMember joinGroup(Long groupId, Long userId) {
        Group group = groupService.findGroupById(groupId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        checkGroupLeader(group,user.getId());

        Optional<GroupMember> existingMember = groupMemberRepository.findByGroupAndUser(group, user);

        if (existingMember.isPresent()) {
            GroupMember member = existingMember.get();
            member.join();
            return member;
        } else {
            GroupMember newMember = GroupMember.create(group, user);
            return groupMemberRepository.save(newMember);
        }

    }

    public GroupMember findByGroupAndUser(Group group, User user) {
        return groupMemberRepository.findByGroupAndUser(group, user)
                .orElseThrow(() -> new GroupMemberException(ErrorCode.GROUP_MEMBER_NOT_FOUND));
    }

    private void checkGroupLeader(Group group, Long userId) {
        if (group.getLeader().getId().equals(userId)) {
            throw new GroupMemberException(ErrorCode.LEADER_CANNOT_JOIN);
        }
    }


    @Transactional
    public void leaveGroup(Long groupId, Long userId) {
        Group group = groupService.findGroupById(groupId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        GroupMember member = findByGroupAndUser(group, user);

        GroupMember.checkLeftGroup(member);


        member.leaveGroup();
    }

    @Transactional
    public void bannedGroup(Long groupId, Long userId) {
        Group group = groupService.findGroupById(groupId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        GroupMember member = findByGroupAndUser(group, user);

        member.bannedGroup();

    }
}