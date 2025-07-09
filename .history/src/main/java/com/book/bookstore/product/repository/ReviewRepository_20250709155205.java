package com.book.bookstore.product.repository;

import com.book.bookstore.product.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 추가적인 검색 메서드는 여기에 정의
} 