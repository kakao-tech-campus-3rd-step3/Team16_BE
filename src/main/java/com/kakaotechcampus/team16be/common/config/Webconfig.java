package com.kakaotechcampus.team16be.common.config;

import com.kakaotechcampus.team16be.common.interceptor.AdminCheckInterceptor;
import com.kakaotechcampus.team16be.common.interceptor.LoginCheckInterceptor;
import com.kakaotechcampus.team16be.common.resolver.LoginUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class Webconfig implements WebMvcConfigurer {

    private final LoginUserArgumentResolver loginUserArgumentResolver;
    private final LoginCheckInterceptor loginCheckInterceptor;
    private final AdminCheckInterceptor adminCheckInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해
                .allowedOrigins("*") // 프론트 배포 주소
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // 필요한 HTTP 메서드 허용
                .allowCredentials(false) // jwt이기 때문에 비허용
                .allowedHeaders("*") // 모든 헤더 허용
                .maxAge(3600); // preflight 요청 캐시 시간
    }

    // Interceptor 등록
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/api/**") // JWT 적용할 경로
                .excludePathPatterns(
                        "/api/auth/kakao-login"
                );

        registry.addInterceptor(adminCheckInterceptor)
                .addPathPatterns(
                        "/api/admin/**",
                        "/admin/**"
                        ); // 관리자만 접근

    }

    // ArgumentResolver 등록
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserArgumentResolver);
    }
}
