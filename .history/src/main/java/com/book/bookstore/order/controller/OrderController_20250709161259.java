package com.book.bookstore.order.controller;

import com.book.bookstore.order.domain.Order;
import com.book.bookstore.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Optional;
import com.book.bookstore.order.domain.OrderItem;
import com.book.bookstore.order.domain.Cart;
import com.book.bookstore.order.domain.CartItem;
import com.book.bookstore.order.service.CartService;
import com.book.bookstore.user.domain.User;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        return ResponseEntity.ok(orderService.createOrder(order));
    }

    @PostMapping("/user")
    public ResponseEntity<?> createOrder(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return ResponseEntity.status(401).body("로그인이 필요합니다.");
        Cart cart = cartService.findOrCreateCartByUser(user);
        if (cart.getCartItems().isEmpty()) return ResponseEntity.badRequest().body("장바구니가 비어 있습니다.");
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(java.time.LocalDateTime.now());
        order.setStatus(Order.Status.PAID); // 결제완료로 처리(카카오페이 연동 예시)
        int total = 0;
        java.util.List<OrderItem> items = new java.util.ArrayList<>();
        for (CartItem ci : cart.getCartItems()) {
            OrderItem oi = OrderItem.builder()
                .order(order)
                .book(ci.getBook())
                .quantity(ci.getQuantity())
                .price(ci.getBook().getPrice() * ci.getQuantity())
                .build();
            items.add(oi);
            total += oi.getPrice();
        }
        order.setOrderItems(items);
        order.setTotalAmount(total);
        orderService.createOrder(order);
        // 장바구니 비우기
        cart.getCartItems().clear();
        cartService.updateCart(cart.getId(), cart);
        return ResponseEntity.ok("주문이 완료되었습니다.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        Optional<Order> order = orderService.getOrder(id);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user")
    public ResponseEntity<java.util.List<Order>> userOrderList(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return ResponseEntity.status(401).body(null);
        java.util.List<Order> orders = orderService.findOrdersByUser(user);
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/user/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return ResponseEntity.status(401).body("로그인이 필요합니다.");
        Order order = orderService.getOrder(orderId).orElse(null);
        if (order == null || !order.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("권한이 없습니다.");
        }
        order.setStatus(Order.Status.CANCELLED);
        orderService.updateOrder(orderId, order);
        return ResponseEntity.ok("주문이 취소되었습니다.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) {
        return ResponseEntity.ok(orderService.updateOrder(id, order));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<Order>> listOrders(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(orderService.listOrders(pageable));
    }

    @GetMapping("/admin")
    public ResponseEntity<Page<Order>> adminListOrders(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String bookName,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String saleStatus,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort
    ) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, org.springframework.data.domain.Sort.by(parseSort(sort)));
        Specification<Order> spec = (root, query, cb) -> {
            java.util.List<jakarta.persistence.criteria.Predicate> predicates = new java.util.ArrayList<>();
            if (StringUtils.hasText(username)) {
                predicates.add(cb.like(root.get("user").get("username"), "%" + username + "%"));
            }
            // Book, publisher, author, saleStatus 등은 join 필요 (예시)
            // ...
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("orderDate"), startDate));
            }
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("orderDate"), endDate));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
        return ResponseEntity.ok(orderService.listOrdersWithSearch(spec, pageable));
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<Order> adminGetOrder(@PathVariable Long id) {
        Optional<Order> order = orderService.getOrder(id);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/admin/{id}/status")
    public ResponseEntity<Order> adminUpdateOrderStatus(@PathVariable Long id, @RequestParam Order.Status status) {
        Optional<Order> orderOpt = orderService.getOrder(id);
        if (orderOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Order order = orderOpt.get();
        order.setStatus(status);
        Order updated = orderService.updateOrder(id, order);
        return ResponseEntity.ok(updated);
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
