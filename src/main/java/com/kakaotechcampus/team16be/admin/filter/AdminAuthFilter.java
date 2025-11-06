package com.kakaotechcampus.team16be.admin.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class AdminAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();

        // 로그인 페이지, 정적 리소스, API 요청은 필터 제외
        if (uri.startsWith("/admin/login") ||
                uri.startsWith("/css") || uri.startsWith("/js") || uri.startsWith("/images") ||
                uri.startsWith("/api") ||
                uri.startsWith("/healthcheck") || uri.startsWith("/actuator/health")) {
            chain.doFilter(request, response);
            return;
        }

        // preflight 요청(OPTIONS)은 인증 없이 통과
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            res.setStatus(HttpServletResponse.SC_OK);
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        boolean isAdmin = session != null && Boolean.TRUE.equals(session.getAttribute("isAdmin"));

        if (!isAdmin) {
            res.sendRedirect("/admin/login");
            return;
        }

        chain.doFilter(request, response);
    }
}
