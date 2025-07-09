package com.book.bookstore.user.controller;

import com.book.bookstore.user.domain.User;
import com.book.bookstore.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import com.book.bookstore.order.domain.Order;
import com.book.bookstore.order.repository.OrderRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        Optional<User> user = userService.getUser(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<User>> listUsers(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(userService.listUsers(pageable));
    }

    @GetMapping("/admin")
    public ResponseEntity<Page<User>> adminListUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) User.Status status,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime endDate,
            @RequestParam(required = false) User.Grade grade,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort
    ) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, org.springframework.data.domain.Sort.by(parseSort(sort)));
        Specification<User> spec = (root, query, cb) -> {
            java.util.List<jakarta.persistence.criteria.Predicate> predicates = new java.util.ArrayList<>();
            if (StringUtils.hasText(username)) {
                predicates.add(cb.like(root.get("username"), "%" + username + "%"));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (StringUtils.hasText(email)) {
                predicates.add(cb.like(root.get("email"), "%" + email + "%"));
            }
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), startDate));
            }
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), endDate));
            }
            if (grade != null) {
                predicates.add(cb.equal(root.get("grade"), grade));
            }
            if (StringUtils.hasText(name)) {
                predicates.add(cb.like(root.get("name"), "%" + name + "%"));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
        return ResponseEntity.ok(userService.listUsersWithSearch(spec, pageable));
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<User> adminGetUser(@PathVariable Long id) {
        Optional<User> user = userService.getUser(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/admin/{id}/orders")
    public ResponseEntity<java.util.List<Order>> adminGetUserOrders(@PathVariable Long id) {
        java.util.List<Order> orders = orderRepository.findAll((root, query, cb) -> cb.equal(root.get("user").get("id"), id));
        return ResponseEntity.ok(orders);
    }

    private org.springframework.data.domain.Sort.Order[] parseSort(String[] sort) {
        return java.util.Arrays.stream(sort)
                .map(s -> {
                    String[] arr = s.split(",");
                    if (arr.length == 2 && arr[1].equalsIgnoreCase("desc")) {
                        return new org.springframework.data.domain.Sort.Order(org.springframework.data.domain.Sort.Direction.DESC, arr[0]);
                    } else {
                        return new org.springframework.data.domain.Sort.Order(org.springframework.data.domain.Sort.Direction.ASC, arr[0]);
                    }
                })
                .toArray(org.springframework.data.domain.Sort.Order[]::new);
    }
} 