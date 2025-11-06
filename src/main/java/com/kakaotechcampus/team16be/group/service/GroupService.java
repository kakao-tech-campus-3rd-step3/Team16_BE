package com.kakaotechcampus.team16be.group.service;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.domain.SafetyTag;
import com.kakaotechcampus.team16be.group.dto.CreateGroupDto;
import com.kakaotechcampus.team16be.group.dto.ResponseGroupListDto;
import com.kakaotechcampus.team16be.group.dto.ResponseSingleGroupDto;
import com.kakaotechcampus.team16be.group.dto.UpdateGroupDto;
import com.kakaotechcampus.team16be.user.domain.User;
import jakarta.validation.Valid;

import java.util.List;

public interface GroupService {
    Group createGroup(User user, CreateGroupDto createGroupDto);

    List<Group> getAllGroups();

    void deleteGroup(User user, Long groupId);

    Group updateGroup(User user, Long groupId, UpdateGroupDto updateGroupDto);

    Group findGroupById(Long groupId);

    Group updateGroupImage(User user, Long groupId, UpdateGroupDto updateGroupDto);

    List<Group> findAll();

    void updateGroupScore(Group group, Double avg);

    void updateGroupTag(Group group);

    Group getGroupById(Long groupId);

    void updateGroupScoreAndTag(Group group, Double newScore);

    void checkGroupLeader(User user, Group targetGroup);
}
