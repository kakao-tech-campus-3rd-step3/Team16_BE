package com.kakaotechcampus.team16be.groupMember.service;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.groupMember.exception.GroupMemberException;
import com.kakaotechcampus.team16be.groupMember.repository.GroupMemberRepository;
import com.kakaotechcampus.team16be.notification.service.NotificationService;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupMemberFacade {

    private final NotificationService notificationService;
    private final GroupMemberService groupMemberService;
    private final GroupService groupService;
    private final UserService userService;
    private final GroupMemberRepository groupMemberRepository;


    @Transactional
    public GroupMember joinGroup(Long groupId, Long joinerId, Long leaderId) throws GroupMemberException {
        Group group = groupService.findGroupById(groupId);

        User leader = userService.findById(leaderId);
        group.checkLeader(leader);

        User joiner = userService.findById(joinerId);

        Optional<GroupMember> existingMember = groupMemberRepository.findByGroupAndUser(group, joiner);

        if (existingMember.isPresent()) {
            GroupMember member = existingMember.get();
            member.acceptJoin();
            notificationService.createGroupJoinNotification(joiner, group);
            return member;
        } else {
            GroupMember newMember = GroupMember.acceptJoin(group, joiner);
            notificationService.createGroupJoinNotification(joiner, group);
            return groupMemberRepository.save(newMember);
        }
    }


    @Transactional
    public GroupMember signGroup(User user, Long groupId,String intro) {
        User signedUser = userService.findById(user.getId());
        Group targetGroup = groupService.findGroupById(groupId);

        GroupMember signMember = GroupMember.sign(signedUser, targetGroup, intro);
        notificationService.createGroupSignNotification(targetGroup.getLeader(), targetGroup);

        return groupMemberRepository.save(signMember);
    }


    @Transactional
    public GroupMember leaveGroup(Long groupId, Long userId) {
        Group group = groupService.findGroupById(groupId);

        User user = userService.findById(userId);

        GroupMember member = groupMemberService.findByGroupAndUser(group, user);

        GroupMember.checkLeftGroup(member);


        member.leaveGroup();
        notificationService.createGroupLeaveNotification(user, group);
        return member;
    }

    @Transactional
    public GroupMember bannedGroup(Long groupId, Long userId, User leaderUser) {
        Group group = groupService.findGroupById(groupId);

        group.checkLeader(leaderUser);

        User bannedUser = userService.findById(userId);

        GroupMember member = groupMemberService.findByGroupAndUser(group, bannedUser);

        member.bannedGroup();

        notificationService.createGroupBannedNotification(bannedUser, group);

        return member;

    }

    public GroupMember rejectJoin(User user, Long groupId, Long userId) {
        Group targetGroup = groupService.findGroupById(groupId);
        targetGroup.checkLeader(user);
        User joinUser = userService.findById(userId);

        GroupMember targetMember = groupMemberService.findByGroupAndUser(targetGroup,joinUser);
        targetMember.rejectJoin();
        notificationService.createGroupRejectNotification(joinUser, targetGroup);

        return targetMember;
    }
}
