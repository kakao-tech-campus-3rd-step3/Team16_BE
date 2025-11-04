package com.kakaotechcampus.team16be.admin.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AdminAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();

        // 로그인 페이지와 리소스는 필터 제외
        if (uri.startsWith("/admin/login") || uri.startsWith("/css") || uri.startsWith("/js") || uri.startsWith("/images")) {
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
