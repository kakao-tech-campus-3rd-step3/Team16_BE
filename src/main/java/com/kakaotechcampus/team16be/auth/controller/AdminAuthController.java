package com.kakaotechcampus.team16be.auth.controller;

import com.kakaotechcampus.team16be.auth.dto.StudentIdImageResponse;
import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {
    private final UserService userService;

    @GetMapping("/student-verification")
    public ResponseEntity<StudentIdImageResponse> getStudentIdImage(
            @LoginUser User adminUser
    ) {
        String imageUrl = userService.getStudentIdImageUrl(adminUser);
        return ResponseEntity.ok(new StudentIdImageResponse(imageUrl));
    }
}
