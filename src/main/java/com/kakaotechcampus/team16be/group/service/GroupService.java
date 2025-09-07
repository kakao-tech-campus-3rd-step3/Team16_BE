package com.kakaotechcampus.team16be.group.service;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.dto.*;

import java.util.List;

public interface GroupService {
    Group createGroup(Long userId, CreateGroupDto createGroupDto);

    List<ResponseGroupListDto> getAllGroups();

    void deleteGroup(Long groupId);

    Group updateGroup(Long id, UpdateGroupDto updateGroupDto);

    Group findGroupById(Long groupId);

    ResponseSingleGroupDto getGroup(Long groupId);
}
