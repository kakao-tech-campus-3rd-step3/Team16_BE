package com.kakaotechcampus.team16be.auth.controller;

import com.kakaotechcampus.team16be.auth.dto.StudentIdImageResponse;
import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
@Tag(name = "인증 인가 관련 API", description = "어드민용 인증 인가 API")
public class AdminAuthController {
    private final UserService userService;

    @Operation(summary = "증빙서류(학생증) 이미지 조회", description = "유저의 증빙서류(학생증) 이미지를 반환합니다.")
    @GetMapping("/student-verification")
    public ResponseEntity<StudentIdImageResponse> getStudentIdImage(
            @LoginUser User adminUser
    ) {
        String imageUrl = userService.getStudentIdImageUrl(adminUser);
        return ResponseEntity.ok(new StudentIdImageResponse(imageUrl));
    }
}
