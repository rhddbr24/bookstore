package com.book.bookstore.order.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OrderController {

    @GetMapping("/book/order")
    public String bookOrder(){
        return "order/book_order";
    }

    @GetMapping("/purchase")
    public String purchase(){
        return "order/purchase";
    }

//    @PostMapping("/")


}
