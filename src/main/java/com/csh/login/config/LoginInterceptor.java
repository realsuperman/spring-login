package com.csh.login.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

public class LoginInterceptor implements HandlerInterceptor {

    // 로그인 없이 접근 가능한 URL 목록 (화이트리스트)
    private final List<String> EXACT_MATCH_WHITE_LIST = List.of("/", "/login");
    private final List<String> PREFIX_MATCH_WHITE_LIST = List.of("/css/", "/js/", "/images/");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        // 화이트리스트에 있는 경우 인터셉터 통과 (로그인 확인 X)
        if (isWhiteListed(requestURI)) {
            return true;
        }

        // 세션 확인 (로그인 여부 체크)
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            return true; // 로그인 상태이면 요청 진행
        }

        // 로그인 안 된 상태이면 로그인 페이지로 리다이렉트
        response.sendRedirect("/");
        return false;
    }

    // 화이트리스트 체크
    private boolean isWhiteListed(String requestURI) {
        return EXACT_MATCH_WHITE_LIST.contains(requestURI) ||
                PREFIX_MATCH_WHITE_LIST.stream().anyMatch(requestURI::startsWith);
    }
}
