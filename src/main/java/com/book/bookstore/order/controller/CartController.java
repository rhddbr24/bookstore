package com.book.bookstore.order.controller;

import com.book.bookstore.order.domain.Cart;
import com.book.bookstore.order.domain.CartItem;
import com.book.bookstore.order.service.CartService;
import com.book.bookstore.product.domain.Book;
import com.book.bookstore.product.repository.BookRepository;
import com.book.bookstore.user.domain.User;
import com.book.bookstore.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/carts")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Cart> createCart(@RequestBody Cart cart) {
        return ResponseEntity.ok(cartService.createCart(cart));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestParam Long bookId, @RequestParam int quantity, HttpSession session) {
        // 로그인 사용자 정보는 세션에서 가져온다고 가정
        User user = (User) session.getAttribute("user");
        if (user == null) return ResponseEntity.status(401).body("로그인이 필요합니다.");
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book == null) return ResponseEntity.badRequest().body("존재하지 않는 상품입니다.");
        // Cart 조회 또는 생성
        Cart cart = cartService.findOrCreateCartByUser(user);
        cartService.addBookToCart(cart, book, quantity);
        return ResponseEntity.ok("장바구니에 추가되었습니다.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCart(@PathVariable Long id) {
        Optional<Cart> cart = cartService.getCart(id);
        return cart.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user")
    public ResponseEntity<java.util.List<CartItem>> userCartList(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return ResponseEntity.status(401).body(null);
        Cart cart = cartService.findOrCreateCartByUser(user);
        return ResponseEntity.ok(cart.getCartItems());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cart> updateCart(@PathVariable Long id, @RequestBody Cart cart) {
        return ResponseEntity.ok(cartService.updateCart(id, cart));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user/{cartItemId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long cartItemId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return ResponseEntity.status(401).body("로그인이 필요합니다.");
        cartService.deleteCartItem(cartItemId, user);
        return ResponseEntity.ok("삭제되었습니다.");
    }

    @GetMapping
    public ResponseEntity<Page<Cart>> listCarts(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(cartService.listCarts(pageable));
    }
} 