package com.kakaotechcampus.team16be.group.controller;


import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.dto.*;
import com.kakaotechcampus.team16be.group.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public ResponseEntity<ResponseGroupDto> createGroup(@LoginUser Long userId, @Valid @RequestBody CreateGroupDto createGroupDto) {
        groupService.createGroup(userId,createGroupDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseGroupDto.success(HttpStatus.CREATED, "모임이 생성되었습니다."));
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
    public ResponseEntity<ResponseGroupDto> deleteGroup(@PathVariable("groupId") Long groupId) {

        groupService.deleteGroup(groupId);

        return ResponseEntity.ok(ResponseGroupDto.success(HttpStatus.OK, "모임이 성공적으로 삭제되었습니다."));
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<ResponseGroupDto> updateGroup(@PathVariable("groupId") Long groupId, @Valid @RequestBody UpdateGroupDto updateGroupDto) {
        groupService.updateGroup(groupId, updateGroupDto);

        return ResponseEntity.ok(ResponseGroupDto.success(HttpStatus.OK, "성공적으로 수정되었습니다."));
    }
}