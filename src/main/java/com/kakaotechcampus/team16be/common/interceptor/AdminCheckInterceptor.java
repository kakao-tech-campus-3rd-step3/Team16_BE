package com.kakaotechcampus.team16be.common.interceptor;

import com.kakaotechcampus.team16be.auth.exception.JwtErrorCode;
import com.kakaotechcampus.team16be.auth.exception.JwtException;
import com.kakaotechcampus.team16be.auth.jwt.JwtProvider;
import com.kakaotechcampus.team16be.user.domain.Role;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AdminCheckInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // preflight 요청은 통과
        if (CorsUtils.isPreFlightRequest(request)) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new JwtException(JwtErrorCode.WRONG_HEADER_TOKEN);
        }

        String token = authHeader.substring(7);
        Claims claims = jwtProvider.parseToken(token);

        String role = (String) claims.get("role");;
//        if (role == null || !role.equals(Role.ADMIN.name())) {
//            throw new JwtException(JwtErrorCode.NOT_ADMIN);
//        }

        return true;
    }
}
