package com.book.bookstore.user.controller;

import com.book.bookstore.user.dto.UserLoginRequest;
import com.book.bookstore.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.Optional;

@Controller
public class loginController {

    @Autowired
    private UserService userService;

    // 로그인 폼
    @GetMapping("/users/login")
    public String loginForm(Model model) {
        model.addAttribute("userLoginRequest", new UserLoginRequest());
        return "user/login";
    }

    // 로그인 처리
    @PostMapping("/users/login")
    public String login(@ModelAttribute UserLoginRequest userLoginRequest, HttpSession session, Model model) {
        boolean success = userService.login(userLoginRequest, session);
        if (success) {
            return "redirect:/";
        } else {
            model.addAttribute("loginError", "아이디 또는 비밀번호가 올바르지 않습니다.");
            return "user/login";
        }
    }

    @PostMapping("/users/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
