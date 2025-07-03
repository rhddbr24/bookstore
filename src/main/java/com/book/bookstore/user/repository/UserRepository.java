package com.book.bookstore.user.repository;

import com.book.bookstore.user.domain.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<user,Integer> {
    Optional<user> findByusername(String username);

}
