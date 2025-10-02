package com.kakaotechcampus.team16be.auth.controller;

import com.kakaotechcampus.team16be.auth.dto.KakaoLoginResponse;
import com.kakaotechcampus.team16be.auth.dto.StudentVerificationStatusResponse;
import com.kakaotechcampus.team16be.auth.dto.UpdateStudentIdImageRequest;
import com.kakaotechcampus.team16be.auth.service.KakaoAuthService;
import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증 인가 관련 API", description = "인증 인가 API")
public class AuthController {

    private final KakaoAuthService kakaoAuthService;
    private final UserService userService;

    @Operation(summary = "카카오 로그인", description = "카카오 인가 코드를 받아 로그인 처리 후 토큰을 프론트로 전달")
    @GetMapping("/kakao-login")
    public void kakaoLogin(
            @RequestParam("code") String code,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        // 카카오 로그인 처리 후 액세스 토큰 DTO 발급
        KakaoLoginResponse kakaoLoginResponse = kakaoAuthService.loginWithCode(code, request);

        // 프론트로 redirect + 토큰 전달
        String redirectUrl = "http://localhost:5173/login?token="
                + kakaoLoginResponse.accessToken(); // DTO에서 액세스 토큰 꺼내서 붙임
        response.sendRedirect(redirectUrl);
    }


    @Operation(summary = "카카오 로그아웃", description = "현재는 액세스 토큰만 사용하고 있어서 별 거 없음. 추후 리프레시 토큰 생기면 블랙리스트 처리해줄 예정입니다.")
    @PostMapping("/kakao-logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        kakaoAuthService.logout(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "증빙서류(학생증) 업로드 완료시", description = "증빙서류(학생증) 이미지를 해당 유저의 DB에 저장합니다.")
    @PutMapping("/student-verification")
    public ResponseEntity<Void> updateStudentIdImage(
            @LoginUser User user,
            @RequestBody UpdateStudentIdImageRequest request
    ) {
        userService.updateStudentIdImage(user.getId(), request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "증빙서류(학생증) 인증 상태 조회", description = "유저의 증빙서류(학생증) 인증 상태를 반환합니다.")
    @GetMapping("/student-verification/status")
    public ResponseEntity<StudentVerificationStatusResponse> getVerificationStatus(
            @LoginUser User user
    ) {
        StudentVerificationStatusResponse response = userService.getVerificationStatus(user.getId());
        return ResponseEntity.ok(response);
    }
}
