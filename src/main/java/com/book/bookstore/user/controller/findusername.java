package com.book.bookstore.user.controller;

import com.book.bookstore.user.dto.useridinfo;
import com.book.bookstore.user.repository.UserRepository;
import com.book.bookstore.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class findusername {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;


    @GetMapping("/findusername")
    public String findname(Model model){
        useridinfo dto = new useridinfo();
        dto.setUserId("asdf");
        model.addAttribute("userid",userService.getEmailById(dto));
        return "user/findusername";
    }
}
