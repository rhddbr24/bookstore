package com.book.bookstore.user.service;

import com.book.bookstore.user.domain.User;
import com.book.bookstore.user.dto.useridinfo;
import com.book.bookstore.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public String getEmailById(Long id){
        return userRepository.findById(id).getEmail();
    }

    @Override
    @Transactional
    public void setEmailById(Long id, String email){
        User user = userRepository.findById(id);
        user.setEmail(email);

        userRepository.save(user);
    }

}
