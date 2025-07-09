package com.book.bookstore.user.service;

import com.book.bookstore.user.domain.User;
import com.book.bookstore.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, User user) {
        user.setId(id);
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Page<User> listUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Page<User> listUsersWithSearch(Specification<User> spec, Pageable pageable) {
        return userRepository.findAll(spec, pageable);
    }

    public boolean existsByUsername(String username) {
        return userRepository.exists((root, query, cb) -> cb.equal(root.get("username"), username));
    }
}
