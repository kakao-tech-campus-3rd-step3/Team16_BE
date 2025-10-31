package com.kakaotechcampus.team16be.admin.controller;

import com.kakaotechcampus.team16be.admin.dto.AdminUserVerificationView;
import com.kakaotechcampus.team16be.admin.service.AdminPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminPageController {

    private final AdminPageService adminPageService;

    @GetMapping("/admin/student-verifications")
    public String getVerificationList(Model model) {
        List<AdminUserVerificationView> pendingList = adminPageService.getAllVerificationRequests();
        model.addAttribute("verifications", pendingList);
        return "admin/adminPage";
    }

    @PostMapping("/admin/student-verifications/update")
    public String updateVerificationStatus(
            @RequestParam("userId") Long userId,
            @RequestParam("status") String status
    ) {
        adminPageService.updateVerificationStatus(userId, status);
        return "redirect:/admin/student-verifications";
    }
}