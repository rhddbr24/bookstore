package com.book.bookstore.product.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BoardController {
    @GetMapping("/user/board")
    public String book(){
        return "book/board";
    }

    @GetMapping("/book/desc")
    public String desc(){
        return "book/desc";
    }




}
