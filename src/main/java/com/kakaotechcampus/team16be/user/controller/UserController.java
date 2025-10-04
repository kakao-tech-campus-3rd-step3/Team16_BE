package com.kakaotechcampus.team16be.user.controller;

import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.groupMember.dto.GroupMemberInfoDto;
import com.kakaotechcampus.team16be.groupMember.service.GroupMemberService;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.dto.*;
import com.kakaotechcampus.team16be.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "유저 관련 API", description = "마이페이지 등에 쓰이는 유저 관련 API들")
public class UserController {

    private final UserService userService;
    private final GroupMemberService groupMemberService;

    @Operation(summary = "프로필 이미지 등록", description = "사용자의 프로필 이미지를 최초 등록합니다.")
    @PostMapping("/profile-image")
    public ResponseEntity<Void> createProfileImage(
            @LoginUser User user,
            @RequestBody UpdateProfileImageRequest request
    ) {
        userService.createProfileImage(user.getId(), request.fileName());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "프로필 이미지 변경", description = "사용자의 기존 프로필 이미지를 새 이미지로 변경합니다.")
    @PutMapping("/profile-image")
    public ResponseEntity<Void> updateProfileImage(
            @LoginUser User user,
            @RequestBody UpdateProfileImageRequest request
    ) {
        userService.updateProfileImage(user.getId(), request.fileName());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "프로필 이미지 조회", description = "사용자의 현재 프로필 이미지를 조회합니다.")
    @GetMapping("/profile-image")
    public ResponseEntity<UserProfileImageResponse> getProfileImage(
            @LoginUser User user
    ) {
        String imageUrl = userService.getProfileImage(user.getId());
        return ResponseEntity.ok(new UserProfileImageResponse(imageUrl));
    }

    @Operation(summary = "프로필 이미지 삭제", description = "사용자의 프로필 이미지를 삭제합니다.")
    @DeleteMapping("/profile-image")
    public ResponseEntity<String> deleteProfileImage(
            @LoginUser User user
    ) {
        userService.deleteProfileImage(user.getId());
        return ResponseEntity.ok("null");
    }

    @Operation(summary = "닉네임 등록", description = "사용자의 닉네임을 최초 등록합니다.")
    @PostMapping("/nickname")
    public ResponseEntity<String> createNickname(
            @LoginUser User user,
            @RequestBody UserNicknameRequest request
    ) {
        userService.createNickname(user, request);
        return ResponseEntity.status(HttpStatus.CREATED).body("닉네임이 설정되었습니다.");
    }

    @Operation(summary = "닉네임 조회", description = "사용자의 현재 닉네임을 조회합니다.")
    @GetMapping("/nickname")
    public ResponseEntity<UserNicknameResponse> getNickname(
            @LoginUser User user
    ) {
        UserNicknameResponse response = userService.getNickname(user);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "닉네임 변경", description = "사용자의 닉네임을 변경합니다.")
    @PutMapping("/nickname")
    public ResponseEntity<String> updateNickname(
            @LoginUser User user,
            @RequestBody UserNicknameRequest request
    ) {
        userService.updateNickname(user, request);
        return ResponseEntity.ok("닉네임이 변경되었습니다.");
    }


    @Operation(summary = "유저 정보 조회", description = "사용자의 정보를 조회합니다.")
    @GetMapping("/info")
    public ResponseEntity<GroupMemberInfoDto> getUserInfo(@LoginUser User user) {
        GroupMemberInfoDto userInfo = groupMemberService.getUserInfo(user);
        return ResponseEntity.ok(userInfo);
    }

}
