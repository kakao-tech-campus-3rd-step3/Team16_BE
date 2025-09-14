package com.kakaotechcampus.team16be.auth.controller;

import com.kakaotechcampus.team16be.auth.dto.KakaoLoginResponse;
import com.kakaotechcampus.team16be.auth.dto.StudentIdImageResponse;
import com.kakaotechcampus.team16be.auth.dto.StudentVerificationStatusResponse;
import com.kakaotechcampus.team16be.auth.dto.UpdateStudentIdImageRequest;
import com.kakaotechcampus.team16be.auth.service.KakaoAuthService;
import com.kakaotechcampus.team16be.common.annotation.AdminOnly;
import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KakaoAuthService kakaoAuthService;
    private final UserService userService;

    @GetMapping("/kakao-login")
    public ResponseEntity<KakaoLoginResponse> kakaoLogin(
            @RequestParam("code") String code,
            HttpServletRequest request
    ) {
        KakaoLoginResponse kakaoLoginResponse = kakaoAuthService.loginWithCode(code, request);
        return ResponseEntity.ok(kakaoLoginResponse);
    }

    @PostMapping("/kakao-logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        kakaoAuthService.logout(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/student-verification")
    public ResponseEntity<Void> updateStudentIdImage(
            @LoginUser User user,
            @RequestBody UpdateStudentIdImageRequest request
    ) {
        userService.updateStudentIdImage(user.getId(), request);
        return ResponseEntity.ok().build();
    }

    @AdminOnly
    @GetMapping("/student-verification")
    public ResponseEntity<StudentIdImageResponse> getStudentIdImage(
            @LoginUser User user
    ) {
        String imageUrl = userService.getStudentIdImageUrl(user);
        return ResponseEntity.ok(new StudentIdImageResponse(imageUrl));
    }

    @GetMapping("/student-verification/status")
    public ResponseEntity<StudentVerificationStatusResponse> getVerificationStatus(
            @LoginUser User user
    ) {
        StudentVerificationStatusResponse response = userService.getVerificationStatus(user.getId());
        return ResponseEntity.ok(response);
    }
}
