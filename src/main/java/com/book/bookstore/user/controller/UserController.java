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
import com.book.bookstore.user.dto.UserRegisterRequest;
import com.book.bookstore.user.dto.UserLoginRequest;
import com.book.bookstore.user.dto.PasswordResetRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequest request) {
        // 유효성 검사
        if (!org.springframework.util.StringUtils.hasText(request.getUsername()) ||
            !org.springframework.util.StringUtils.hasText(request.getPassword()) ||
            !org.springframework.util.StringUtils.hasText(request.getEmail())) {
            return ResponseEntity.badRequest().body("필수 입력값이 누락되었습니다.");
        }
        if (userService.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("이미 사용중인 아이디입니다.");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setStatus(User.Status.ACTIVE);
        user.setGrade(User.Grade.BASIC);
        user.setCreatedAt(java.time.LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userService.createUser(user);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest request, HttpSession session) {
        Optional<User> userOpt = userService.findByUsername(request.getUsername());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("존재하지 않는 아이디입니다.");
        }
        User user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("비밀번호가 일치하지 않습니다.");
        }
        session.setAttribute("user", user);
        return ResponseEntity.ok("로그인 성공");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("로그아웃 성공");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest request) {
        Optional<User> userOpt = userService.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("존재하지 않는 이메일입니다.");
        }
        User user = userOpt.get();
        String tempPassword = "temp1234";
        user.setPassword(passwordEncoder.encode(tempPassword));
        userService.updateUser(user.getId(), user);
        return ResponseEntity.ok("임시 비밀번호가 이메일로 전송되었습니다. (예시)");
    }

    @GetMapping("/check-id")
    public ResponseEntity<?> checkId(@RequestParam String username) {
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(!exists);
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