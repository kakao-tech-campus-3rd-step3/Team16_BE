package com.kakaotechcampus.team16be.admin.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminAuthPageController {

    @Value("${admin.password}")
    private String ADMIN_PASSWORD;

    @GetMapping("/login")
    public String loginPage() {
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam("password") String password,
            HttpSession session,
            Model model
    ) {
        if (ADMIN_PASSWORD.equals(password)) {
            session.setAttribute("isAdmin", true);
            return "redirect:/admin/student-verifications";
        } else {
            model.addAttribute("error", "비밀번호가 올바르지 않습니다.");
            return "admin/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
}
