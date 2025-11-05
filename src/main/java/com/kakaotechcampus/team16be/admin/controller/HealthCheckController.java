package com.kakaotechcampus.team16be.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HealthCheckController {

    @GetMapping("/healthcheck")
    public String healthcheck() {
        return "OK";
    }

}
