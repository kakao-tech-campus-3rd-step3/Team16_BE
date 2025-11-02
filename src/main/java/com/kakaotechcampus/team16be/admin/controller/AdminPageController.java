package com.kakaotechcampus.team16be.admin.controller;

import com.kakaotechcampus.team16be.admin.dto.AdminUserVerificationView;
import com.kakaotechcampus.team16be.admin.service.AdminPageService;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.domain.SafetyTag;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminPageController {

    private final AdminPageService adminPageService;
    private final UserService userService;
    private final GroupService groupService;

    // 학생증 인증 요청 관리 페이지
    @GetMapping("/student-verifications")
    public String getVerificationList(Model model) {
        List<AdminUserVerificationView> pendingList = adminPageService.getAllVerificationRequests();
        model.addAttribute("verifications", pendingList);
        return "admin/adminPage";
    }

    @PostMapping("/student-verifications/update")
    public String updateVerificationStatus(
            @RequestParam("userId") Long userId,
            @RequestParam("status") String status
    ) {
        adminPageService.updateVerificationStatus(userId, status);
        return "redirect:/admin/student-verifications";
    }

    // 유저 참여도 점수 관리 페이지
    @GetMapping("/scores")
    public String getUserScores(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/adminScores";
    }

    @PostMapping("/scores/update")
    public String updateUserScore(
            @RequestParam("userId") Long userId,
            @RequestParam("newScore") Double newScore
    ) {
        userService.updateUserScore(userId, newScore);
        return "redirect:/admin/scores";
    }

    // 그룹 안전 태그(상태) 관리 페이지 (점수를 변경하면 태그가 수정되게끔)
    @GetMapping("/groups")
    public String getGroupSafetyStatus(Model model) {
        model.addAttribute("groups", groupService.getAllGroups());
        model.addAttribute("tags", SafetyTag.values());
        return "admin/adminGroups";
    }

    @PostMapping("/groups/update")
    public String updateGroupScore(
            @RequestParam("groupId") Long groupId,
            @RequestParam("newScore") Double newScore
    ) {
        Group group = groupService.getGroupById(groupId);
        groupService.updateGroupScoreAndTag(group, newScore);
        return "redirect:/admin/groups";
    }
}