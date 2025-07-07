package com.book.bookstore.user.service;

import com.book.bookstore.user.domain.User;
import com.book.bookstore.user.dto.useridinfo;
import com.book.bookstore.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public String getEmailById(useridinfo dto){
        return userRepository.findById(dto.getUserId()).getEmail();
    }

}
