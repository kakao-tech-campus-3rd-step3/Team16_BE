package com.kakaotechcampus.team16be.group.service;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.dto.CreateGroupDto;
import com.kakaotechcampus.team16be.group.dto.ResponseGroupListDto;
import com.kakaotechcampus.team16be.group.dto.ResponseSingleGroupDto;
import com.kakaotechcampus.team16be.group.dto.UpdateGroupDto;

import java.util.List;

public interface GroupService {
    Group createGroup(Long userId, CreateGroupDto createGroupDto);

    List<ResponseGroupListDto> getAllGroups();

    void deleteGroup(Long groupId);

    Group updateGroup(Long userId, Long groupId, UpdateGroupDto updateGroupDto);

    Group findGroupById(Long groupId);

    ResponseSingleGroupDto getGroup(Long groupId);
}
