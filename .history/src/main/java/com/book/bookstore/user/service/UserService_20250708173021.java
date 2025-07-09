package com.book.bookstore.user.service;

import com.book.bookstore.user.domain.User;
import com.book.bookstore.user.dto.useridinfo;

public interface UserService {
    public String getEmailById(Long id);

    public void setEmailById(Long id, String email);
}
