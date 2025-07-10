package com.book.bookstore.user.controller;

import com.book.bookstore.user.model.User;
import com.book.bookstore.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String register() {
        return "user/register";
    }

    @PostMapping("/register")
    public String registersuccess(@RequestParam String username,
                                  @RequestParam String password,
                                  @RequestParam String name,
                                  @RequestParam String email,
                                  @RequestParam(required = false) String phone,
                                  @RequestParam(required = false) String address) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // 비밀번호 암호화는 UserService에서 처리
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);
        user.setCreatedAt(LocalDateTime.now());
        user.setGrade(User.Grade.BASIC);
        user.setStatus(User.Status.ACTIVE);

        userService.createUser(user);
        return "redirect:/users/login";
    }
}
