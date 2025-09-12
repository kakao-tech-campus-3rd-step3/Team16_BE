package com.kakaotechcampus.team16be.groupMember.service;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.user.domain.User;

public interface GroupMemberService {
    GroupMember joinGroup(Long groupId, Long userId);

    void leaveGroup(Long groupId, Long userId);

    void bannedGroup(Long groupId, Long userId);

    GroupMember findByGroupAndUser(Group group, User user);

    boolean checkMemberHasLeft(Group targetGroup, User user);
}
