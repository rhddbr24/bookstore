package com.book.bookstore.order.repository;

import com.book.bookstore.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // 추가적인 검색 메서드는 여기에 정의
} 