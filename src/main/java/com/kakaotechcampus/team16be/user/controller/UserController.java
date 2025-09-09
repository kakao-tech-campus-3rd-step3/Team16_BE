package com.kakaotechcampus.team16be.user.controller;

import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.dto.UpdateProfileImageRequest;
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

}
