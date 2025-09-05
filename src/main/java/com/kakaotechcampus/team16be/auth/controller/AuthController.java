package com.kakaotechcampus.team16be.auth.controller;

import com.kakaotechcampus.team16be.auth.dto.KakaoLoginResponse;
import com.kakaotechcampus.team16be.auth.service.KakaoAuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final KakaoAuthService kakaoAuthService;

    public AuthController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

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
}
