package com.kakaotechcampus.team16be.group.service;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.dto.CreateGroupDto;
import com.kakaotechcampus.team16be.group.dto.UpdateGroupDto;

import java.util.List;

public interface GroupService {
    Group createGroup(CreateGroupDto createGroupDto);

    List<Group> getAllGroups();

    void deleteGroup(Long groupId);

    Group updateGroup(Long id, UpdateGroupDto updateGroupDto);
}
