package com.book.bookstore.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class loginController {
    @GetMapping("users/login")
    public String login(){
        return "user/login";
    }

    @PostMapping("users/login")
    public String loginCheck(){
        return "redirect:/user/board";
    }
}
