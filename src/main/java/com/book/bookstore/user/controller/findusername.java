package com.book.bookstore.user.controller;

import com.book.bookstore.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class findusername {

    @Autowired
    UserService userService;


    @GetMapping("/findusername")
    public String findname(Model model){
        model.addAttribute("userid",userService.getEmailById(1L));
        return "user/findusername";
    }

    @GetMapping("/updateemail")
    public String updateemail(Model model){
        userService.setEmailById(1L,"fdsa@naver.com");
        model.addAttribute("email",userService.getEmailById(1L));
        return  "user/updateemail";
    }
}
