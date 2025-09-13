package com.kakaotechcampus.team16be.groupMember.controller;


import com.amazonaws.Response;
import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.groupMember.dto.RequestGroupMemberDto;
import com.kakaotechcampus.team16be.groupMember.dto.ResponseGroupMemberDto;
import com.kakaotechcampus.team16be.groupMember.service.GroupMemberService;
import com.kakaotechcampus.team16be.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/group")
public class GroupMemberController {

    private final GroupMemberService groupMemberService;

    @PostMapping("/join")
    public ResponseEntity<ResponseGroupMemberDto> joinGroup(@LoginUser User user, RequestGroupMemberDto requestGroupMemberDto) {
        groupMemberService.joinGroup(requestGroupMemberDto.groupId(), user.getId());

        return ResponseEntity.ok(ResponseGroupMemberDto.success(HttpStatus.CREATED, "해당 유저를 그룹에 가입 승인했습니다"));
    }

    @PostMapping("/leave")
    public ResponseEntity<ResponseGroupMemberDto> leaveGroup(@LoginUser User user, RequestGroupMemberDto requestGroupMemberDto) {
        groupMemberService.leaveGroup(requestGroupMemberDto.groupId(), user.getId());

        return ResponseEntity.ok(ResponseGroupMemberDto.success(HttpStatus.OK, user.getNickname() + "가 그룹을 탈퇴했습니다."));
    }

    @PostMapping("/banned")
    public ResponseEntity<ResponseGroupMemberDto> bannedMember(@LoginUser User user, RequestGroupMemberDto requestGroupMemberDto) {
        groupMemberService.bannedGroup(requestGroupMemberDto.groupId(), requestGroupMemberDto.userId(), user);

        return ResponseEntity.ok(ResponseGroupMemberDto.success(HttpStatus.OK, user.getNickname() + "가 그룹에서 강퇴당했습니다"));
    }
}
