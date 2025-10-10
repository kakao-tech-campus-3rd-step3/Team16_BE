package com.kakaotechcampus.team16be.groupMember.controller;


import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.groupMember.dto.*;
import com.kakaotechcampus.team16be.groupMember.service.GroupMemberService;
import com.kakaotechcampus.team16be.groupMember.service.GroupMemberFacade;
import com.kakaotechcampus.team16be.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/group")
@Tag(name = "그룹 멤버 API", description = "그룹 멤버 관련 API")
public class GroupMemberController {

    private final GroupMemberService groupMemberService;
    private final GroupMemberFacade GroupMemberFacade;

    @Operation(summary = "그룹 가입 승인", description = "특정 유저를 그룹에 가입 승인합니다.")
    @PostMapping("/join")
    public ResponseEntity<ResponseGroupMemberDto> joinGroup(@LoginUser User user, @RequestBody ApproveJoinRequestDto approveJoinRequestDto) {
        GroupMemberFacade.joinGroup(approveJoinRequestDto.groupId(), approveJoinRequestDto.userId(), user.getId());

        return ResponseEntity.ok(ResponseGroupMemberDto.success(HttpStatus.CREATED, "해당 유저를 그룹에 가입 승인했습니다"));
    }

    @Operation(summary = "그룹 탈퇴", description = "로그인한 유저를 해당 그룹에서 탈퇴 처리합니다.")
    @PostMapping("/leave")
    public ResponseEntity<ResponseGroupMemberDto> leaveGroup(@LoginUser User user, @RequestBody LeaveGroupRequestDto leaveGroupRequestDto) {
        GroupMember groupMember = GroupMemberFacade.leaveGroup(leaveGroupRequestDto.groupId(), user.getId());

        return ResponseEntity.ok(ResponseGroupMemberDto.success(HttpStatus.OK, groupMember.getUser().getNickname() + "가 그룹을 탈퇴했습니다."));
    }

    @Operation(summary = "그룹 강퇴", description = "특정 유저를 그룹에서 강제로 제거합니다.")
    @PostMapping("/banned")
    public ResponseEntity<ResponseGroupMemberDto> bannedMember(@LoginUser User user, @RequestBody BanMemberRequestDto banMemberRequestDto) {
        GroupMember groupMember = GroupMemberFacade.bannedGroup(banMemberRequestDto.groupId(), banMemberRequestDto.userId(), user);


        return ResponseEntity.ok(ResponseGroupMemberDto.success(HttpStatus.OK, groupMember.getUser().getNickname() + "가 그룹에서 강퇴당했습니다"));
    }

    @Operation(summary = "그룹 가입 신청 취소", description = "특정 유저가 그룹 가입 신청을 취소합니다.")
    @PostMapping("/sign/cancel")
    public ResponseEntity<ResponseGroupMemberDto> cancelSignGroup(@LoginUser User user, @RequestBody CancelSignRequestDto cancelSignRequestDto) {
        groupMemberService.cancelSignGroup(user, cancelSignRequestDto.groupId());
        return ResponseEntity.ok(ResponseGroupMemberDto.success(HttpStatus.OK, "가입신청을 취소했습니다."));
    }

    @Operation(summary = "그룹 가입 신청", description = "로그인한 유저가 그룹 가입 신청을 합니다.")
    @PostMapping("/sign")
    public ResponseEntity<ResponseGroupMemberDto> signGroup(@LoginUser User user, @RequestBody SignGroupRequestDto signGroupRequestDto) {
        GroupMember groupMember = GroupMemberFacade.signGroup(user, signGroupRequestDto.groupId(), signGroupRequestDto.intro());

        return ResponseEntity.ok(ResponseGroupMemberDto.success(HttpStatus.CREATED, "가입신청을 완료했습니다."));
    }

    @Operation(summary = "그룹장 위임", description = "특정 유저에게 그룹장 권한을 위임합니다.")
    @PutMapping("/change")
    public ResponseEntity changeGroupLeader(@LoginUser User user, @RequestBody ChangeLeaderRequestDto changeLeaderRequestDto) {
        groupMemberService.changeLeader(changeLeaderRequestDto.groupId(), user, changeLeaderRequestDto.userId());

        return ResponseEntity.ok(ResponseGroupMemberDto.success(HttpStatus.OK, "그룹장 위임이 완료되었습니다."));
    }

    @Operation(
            summary = "모임 가입 신청 목록 조회",
            description = "그룹장이 모임에 가입 신청한 사용자 목록을 조회합니다."
    )
    @GetMapping("/{groupId}/join-requests")
    public ResponseEntity<List<SignResponseDto>> getAllJoinRequest(
            @LoginUser User user, @PathVariable Long groupId
    ) {
        List<SignResponseDto> members = groupMemberService.findByGroupAndPendingUser(user, groupId);

        return ResponseEntity.ok(members);
    }

    @Operation(summary = "그룹별 멤버", description = "해당 그룹에 가입한 멤버 반환")
    @GetMapping("/{groupId}")
    public ResponseEntity<List<GroupMemberDto>> getGroupMember(@LoginUser User user, @PathVariable Long groupId) {
        List<GroupMember> members = groupMemberService.getGroupMember(user, groupId);

        return ResponseEntity.ok(GroupMemberDto.from(members));
    }
}
