package com.kakaotechcampus.team16be.auth.controller;

import com.kakaotechcampus.team16be.auth.dto.KakaoLoginResponse;
import com.kakaotechcampus.team16be.auth.dto.StudentVerificationStatusResponse;
import com.kakaotechcampus.team16be.auth.dto.UpdateStudentIdImageRequest;
import com.kakaotechcampus.team16be.auth.service.KakaoAuthService;
import com.kakaotechcampus.team16be.common.annotation.LoginUser;
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

    @PostMapping("/kakao-login")
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
            @LoginUser Long userId,
            @RequestBody UpdateStudentIdImageRequest request
    ) {
        userService.updateStudentIdImage(userId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/student-verification/status")
    public ResponseEntity<StudentVerificationStatusResponse> getVerificationStatus(
            @LoginUser Long userId
    ) {
        StudentVerificationStatusResponse response = userService.getVerificationStatus(userId);
        return ResponseEntity.ok(response);
    }
}
