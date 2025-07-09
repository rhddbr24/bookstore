package com.book.bookstore.order.service;

import com.book.bookstore.order.domain.Order;
import com.book.bookstore.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Optional<Order> getOrder(Long id) {
        return orderRepository.findById(id);
    }

    public Order updateOrder(Long id, Order order) {
        order.setId(id);
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public Page<Order> listOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Page<Order> listOrdersWithSearch(Specification<Order> spec, Pageable pageable) {
        return orderRepository.findAll(spec, pageable);
    }

    public java.util.List<Order> findOrdersByUser(com.book.bookstore.user.domain.User user) {
        return orderRepository.findAll((root, query, cb) -> cb.equal(root.get("user"), user));
    }
} 