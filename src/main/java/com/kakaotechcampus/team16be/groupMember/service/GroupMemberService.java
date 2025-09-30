package com.kakaotechcampus.team16be.groupMember.service;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.user.domain.User;

import java.util.List;

public interface GroupMemberService {

    GroupMember findByGroupAndUser(Group group, User user);

    boolean checkMemberHasLeft(Group targetGroup, User user);

    void createGroup(Group createdGroup, User user);

    void changeLeader(Long groupId, User oldLeader, Long newLeaderId);

    List<GroupMember> findByGroup(Group targetGroup);

    GroupMember cancelSignGroup(User user, Long groupId);

    void validateGroupMember(User user, Long groupId);

    List<GroupMember> findByGroupAndPendingUser(User user, Long groupId);
}
