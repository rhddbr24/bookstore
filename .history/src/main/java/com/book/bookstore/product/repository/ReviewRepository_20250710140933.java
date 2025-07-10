package com.book.bookstore.product.repository;

import com.book.bookstore.product.domain.Review;
import com.book.bookstore.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 사용자별 리뷰 조회
    List<Review> findByUser(User user);
    
    // 책 ID별 리뷰 조회
    List<Review> findByBookId(Long bookId);
} 