package com.kakaotechcampus.team16be.group.controller;


import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.dto.*;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.user.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<ResponseCreateGroupDto> createGroup(@LoginUser User user, @Valid @RequestBody CreateGroupDto createGroupDto) {
        Group group = groupService.createGroup(user, createGroupDto);

        return ResponseEntity.ok(ResponseCreateGroupDto.from(group));
    }

    @GetMapping
    public ResponseEntity<List<ResponseGroupListDto>> getAllGroups() {

        List<ResponseGroupListDto> result = groupService.getAllGroups();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<ResponseSingleGroupDto> getGroup(@PathVariable("groupId") Long groupId) {
        return ResponseEntity.ok(groupService.getGroup(groupId));
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<ResponseGroupDto> deleteGroup(@LoginUser User user,@PathVariable("groupId") Long groupId) {

        groupService.deleteGroup(user,groupId);

        return ResponseEntity.ok(ResponseGroupDto.success(HttpStatus.OK, "모임이 성공적으로 삭제되었습니다."));
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<ResponseUpdateGroupDto> updateGroup(@LoginUser User user, @PathVariable("groupId") Long groupId, @Valid @RequestBody UpdateGroupDto updateGroupDto) {
        Group group = groupService.updateGroup(user, groupId, updateGroupDto);

        return ResponseEntity.ok(ResponseUpdateGroupDto.from(group));
    }
}