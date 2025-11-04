package com.kakaotechcampus.team16be.group.controller;


import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.dto.*;
import com.kakaotechcampus.team16be.group.service.GroupFacade;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
@Tag(name = "모임 API", description = "모임 관련 API")
public class GroupController {

    private final GroupService groupService;
    private final GroupFacade groupFacade;

    @Operation(summary = "모임 생성", description = "새로운 모임을 생성합니다.")
    @PostMapping
    public ResponseEntity<ResponseCreateGroupDto> createGroup(@LoginUser User user, @Valid @RequestBody CreateGroupDto createGroupDto) {
        Group group = groupService.createGroup(user, createGroupDto);
        return ResponseEntity.ok(ResponseCreateGroupDto.from(group));
    }

    @Operation(summary = "모임 목록 조회", description = "모든 모임 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<ResponseGroupListDto>> getAllGroups() {
        List<ResponseGroupListDto> result = groupFacade.getGroups();
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "모임 상세 조회", description = "특정 모임의 상세 정보를 조회합니다.")
    @GetMapping("/{groupId}")
    public ResponseEntity<ResponseSingleGroupDto> getGroup(@PathVariable("groupId") Long groupId) {
        ResponseSingleGroupDto responseSingleGroupDto = groupFacade.getGroup(groupId);
        return ResponseEntity.ok(responseSingleGroupDto);
    }

    @Operation(summary = "모임 삭제", description = "특정 모임을 삭제합니다.")
    @DeleteMapping("/{groupId}")
    public ResponseEntity<ResponseGroupDto> deleteGroup(@LoginUser User user, @PathVariable("groupId") Long groupId) {
        groupService.deleteGroup(user, groupId);
        return ResponseEntity.ok(ResponseGroupDto.success(HttpStatus.OK, "모임이 성공적으로 삭제되었습니다."));
    }

    @Operation(summary = "모임 정보 수정", description = "특정 모임의 정보를 수정합니다.")
    @PutMapping("/{groupId}")
    public ResponseEntity<ResponseUpdateGroupDto> updateGroup(@LoginUser User user, @PathVariable("groupId") Long groupId, @Valid @RequestBody UpdateGroupDto updateGroupDto) {
        Group group = groupService.updateGroup(user, groupId, updateGroupDto);
        return ResponseEntity.ok(ResponseUpdateGroupDto.from(group));
    }

    @Operation(summary = "모임 이미지 수정", description = "특정 모임의 대표 이미지를 수정합니다.")
    @PutMapping("/{groupId}/image")
    public ResponseEntity<ResponseUpdateGroupDto> updateGroupImage(@LoginUser User user, @PathVariable("groupId") Long groupId, @Valid @RequestBody UpdateGroupDto updateGroupDto) {
        Group group = groupService.updateGroupImage(user, groupId, updateGroupDto);
        return ResponseEntity.ok(ResponseUpdateGroupDto.from(group));
    }
}