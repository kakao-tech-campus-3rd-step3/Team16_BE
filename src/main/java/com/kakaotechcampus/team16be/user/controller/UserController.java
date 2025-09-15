package com.kakaotechcampus.team16be.user.controller;

import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.dto.UpdateProfileImageRequest;
import com.kakaotechcampus.team16be.user.dto.UserNicknameRequest;
import com.kakaotechcampus.team16be.user.dto.UserNicknameResponse;
import com.kakaotechcampus.team16be.user.dto.UserProfileImageResponse;
import com.kakaotechcampus.team16be.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 프로필 이미지 설정 (최초 등록)
     */
    @PostMapping("/profile-image")
    public ResponseEntity<Void> createProfileImage(
            @LoginUser User user,
            @RequestBody UpdateProfileImageRequest request
    ) {
        userService.createProfileImage(user.getId(), request.fileName());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 프로필 이미지 변경
     */
    @PutMapping("/profile-image")
    public ResponseEntity<Void> updateProfileImage(
            @LoginUser User user,
            @RequestBody UpdateProfileImageRequest request
    ) {
        userService.updateProfileImage(user.getId(), request.fileName());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 프로필 이미지 조회
     */
    @GetMapping("/profile-image")
    public ResponseEntity<UserProfileImageResponse> getProfileImage(
            @LoginUser User user
    ) {
        String imageUrl = userService.getProfileImage(user.getId());
        return ResponseEntity.ok(new UserProfileImageResponse(imageUrl));
    }

    /**
     * 프로필 이미지 삭제
     */
    @DeleteMapping("/profile-image")
    public ResponseEntity<Void> deleteProfileImage(
            @LoginUser User user
    ) {
        userService.deleteProfileImage(user.getId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /** 닉네임 최초 등록 */
    @PostMapping("/nickname")
    public ResponseEntity<String> createNickname(
            @LoginUser User user,
            @RequestBody UserNicknameRequest request
    ) {
        userService.createNickname(user, request);
        return ResponseEntity.status(HttpStatus.CREATED).body("닉네임이 설정되었습니다.");
    }

    /** 닉네임 조회 */
    @GetMapping("/nickname")
    public ResponseEntity<UserNicknameResponse> getNickname(
            @LoginUser User user
    ) {
        UserNicknameResponse response = userService.getNickname(user);
        return ResponseEntity.ok(response);
    }

    /** 닉네임 수정 */
    @PutMapping("/nickname")
    public ResponseEntity<String> updateNickname(
            @LoginUser User user,
            @RequestBody UserNicknameRequest request
    ) {
        userService.updateNickname(user, request);
        return ResponseEntity.ok("닉네임이 변경되었습니다.");
    }

}
