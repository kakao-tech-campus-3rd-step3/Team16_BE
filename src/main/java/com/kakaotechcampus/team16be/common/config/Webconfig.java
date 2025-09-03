package com.kakaotechcampus.team16be.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Webconfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해
                .allowedOrigins("front-server-address") // 프론트 배포 주소
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 필요한 HTTP 메서드 허용
                .allowCredentials(false) // jwt이기 때문에 비허용
                .allowedHeaders("*") // 모든 헤더 허용
                .maxAge(3600); // preflight 요청 캐시 시간
    }
}
