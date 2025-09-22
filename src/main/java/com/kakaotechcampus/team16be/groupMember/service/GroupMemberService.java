package com.kakaotechcampus.team16be.groupMember.service;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.user.domain.User;

public interface GroupMemberService {
    GroupMember joinGroup(Long groupId, Long joinerId,Long userId);

    GroupMember leaveGroup(Long groupId, Long userId);

    GroupMember bannedGroup(Long groupId, Long userId, User leader);

    GroupMember findByGroupAndUser(Group group, User user);

    boolean checkMemberHasLeft(Group targetGroup, User user);

    void createGroup(Group createdGroup, User user);

    GroupMember signGroup(User user, Long groupId);

    void changeLeader(Long groupId, User oldLeader, Long newLeaderId);

    void validateGroupMember(User user, Long groupId);

}
