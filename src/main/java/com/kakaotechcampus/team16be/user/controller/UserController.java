package com.kakaotechcampus.team16be.user.controller;

import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.dto.*;
import com.kakaotechcampus.team16be.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "유저 관련 API", description = "마이페이지 등에 쓰이는 유저 관련 API들")
public class UserController {

    private final UserService userService;

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

    @Operation(summary = "로그인한 사용자 본인의 프로필 및 그룹 소속 정보 조회", description = "JWT를 통해 로그인된 사용자의 프로필 및 그룹 소속 정보를 반환합니다.")
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getMyInfo(
            @LoginUser User user
    ) {
        UserInfoResponse response = userService.getUserInfo(user.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "특정 사용자의 프로필 및 그룹 소속 정보 조회", description = "사용자의 프로필 및 그룹 소속 정보를 반환합니다.")
    @GetMapping("/{userId}/me")
    public ResponseEntity<UserInfoResponse> getUserInfo(
            @PathVariable Long userId
    ) {
        UserInfoResponse response = userService.getUserInfo(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "사용자의 그룹 활동 내역 조회 (나의활동이력)", description = "사용자의 그룹 참여 및 탈퇴 이력을 반환합니다.")
    @GetMapping("/{userId}/groups/history")
    public ResponseEntity<List<UserGroupHistoryResponse>> getUserGroupHistory(
            @PathVariable Long userId
    ) {
        List<UserGroupHistoryResponse> response = userService.getUserGroupHistory(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "회원 탈퇴", description = "현재 로그인된 유저의 모든 정보를 DB에서 삭제합니다. 연관된 모든 데이터(게시글, 댓글, 그룹 정보 등)도 함께 삭제됩니다.")
    @DeleteMapping("/withdraw")
    public ResponseEntity<Void> withdrawUser(
            @LoginUser User user
    ) {
        userService.withdrawUser(user.getId());
        return ResponseEntity.noContent().build();
    }

}
