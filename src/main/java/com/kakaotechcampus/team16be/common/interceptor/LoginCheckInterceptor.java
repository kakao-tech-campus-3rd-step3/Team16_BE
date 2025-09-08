package com.kakaotechcampus.team16be.common.interceptor;

import com.kakaotechcampus.team16be.auth.exception.JwtErrorCode;
import com.kakaotechcampus.team16be.auth.exception.JwtException;
import com.kakaotechcampus.team16be.auth.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // preflight 요청은 통과
        if (CorsUtils.isPreFlightRequest(request)) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");

        // Authorization 헤더가 없거나 Bearer 형식이 아니면
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new JwtException(JwtErrorCode.WRONG_HEADER_TOKEN);
        }

        String token = authHeader.substring(7); // "Bearer " 제거
        Claims claims = jwtProvider.parseToken(token); // 유효성 검증 + claims 추출

        // 사용자 ID를 request에 담아서 이후 컨트롤러에서 꺼내 쓸 수 있게
        request.setAttribute("userId", claims.getSubject());

        return true;
    }
}
