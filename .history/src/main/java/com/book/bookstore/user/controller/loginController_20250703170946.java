package com.book.bookstore.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class loginController {
    @GetMapping("/login")
    public String login(){
        return "user/login";
    }

    @PostMapping("/login")
    public String loginCheck(){
        return "redirect:/user/board";
    }
}
