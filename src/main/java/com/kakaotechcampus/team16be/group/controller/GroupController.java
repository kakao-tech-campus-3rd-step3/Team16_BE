package com.kakaotechcampus.team16be.group.controller;


import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.dto.CreateGroupDto;
import com.kakaotechcampus.team16be.group.dto.ResponseGroupDto;
import com.kakaotechcampus.team16be.group.dto.ResponseGroupListDto;
import com.kakaotechcampus.team16be.group.dto.UpdateGroupDto;
import com.kakaotechcampus.team16be.group.service.GroupService;
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
    public ResponseEntity<ResponseGroupDto> createGroup(@RequestBody CreateGroupDto createGroupDto) {
        groupService.createGroup(createGroupDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseGroupDto.success(HttpStatus.CREATED, "모임이 생성되었습니다."));
    }

    @GetMapping
    public ResponseEntity<List<ResponseGroupListDto>> getAllGroups() {
        List<Group> groupList = groupService.getAllGroups();

        List<ResponseGroupListDto> result = ResponseGroupListDto.from(groupList);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseGroupDto> deleteGroup(@PathVariable("id") Long id) {

        groupService.deleteGroup(id);

        return ResponseEntity.ok(ResponseGroupDto.success(HttpStatus.OK, "모임이 성공적으로 삭제되었습니다."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseGroupDto> updateGroup(@PathVariable("id") Long id, @RequestBody UpdateGroupDto updateGroupDto) {
        groupService.updateGroup(id, updateGroupDto);

        return ResponseEntity.ok(ResponseGroupDto.success(HttpStatus.OK, "성공적으로 수정되었습니다."));
    }
}