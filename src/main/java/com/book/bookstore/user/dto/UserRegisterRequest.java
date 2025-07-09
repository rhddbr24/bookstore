package com.book.bookstore.user.dto;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private String username;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String password;
} 