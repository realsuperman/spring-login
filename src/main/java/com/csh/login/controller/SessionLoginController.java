package com.csh.login.controller;

import com.csh.login.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class SessionLoginController {
    private final UserService userService;

    // 기본 페이지 (index.html)
    @GetMapping("/")
    public String index(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/home"; // 로그인 상태면 home으로 이동
        }
        return "index"; // 로그인 페이지 반환
    }

    // 홈 페이지 (home.html)
    @GetMapping("/home")
    public String home(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/"; // 로그인 안 했으면 로그인 페이지로 이동
        }
        return "home"; // home.html 반환
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        if (!userService.validateUser(username, password)) {
            return "redirect:/?error=1"; // 로그인 실패 시 index.html로 이동
        }

        session.setAttribute("user", username); // 세션에 사용자 정보 저장
        return "redirect:/home"; // 로그인 성공 시 home.html로 이동
    }

    @GetMapping("/profile")
    public String profile(HttpSession session) {
        String user = (String) session.getAttribute("user");
        if (user == null) {
            return "redirect:/"; // 로그인 안 했으면 로그인 페이지로 이동
        }
        return "profile"; // profile.html 반환
    }

    // 로그아웃 처리
    @PostMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        session.invalidate(); // 세션 무효화

        // JSESSIONID 쿠키 삭제
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setMaxAge(0); // 쿠키 즉시 만료
        cookie.setPath("/"); // 애플리케이션 전체에서 삭제 적용
        response.addCookie(cookie);

        return "redirect:/";
    }

}
