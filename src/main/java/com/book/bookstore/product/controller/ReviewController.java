package com.book.bookstore.product.controller;

import com.book.bookstore.product.domain.Review;
import com.book.bookstore.product.service.ReviewService;
import com.book.bookstore.user.domain.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping("/user")
    public ResponseEntity<?> createReview(@RequestBody Review review, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return ResponseEntity.status(401).body("로그인이 필요합니다.");
        review.setUser(user);
        review.setCreatedAt(java.time.LocalDateTime.now());
        reviewService.createReview(review);
        return ResponseEntity.ok("리뷰가 등록되었습니다.");
    }

    @GetMapping("/user")
    public ResponseEntity<java.util.List<Review>> userReviewList(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return ResponseEntity.status(401).body(null);
        java.util.List<Review> reviews = reviewService.findReviewsByUser(user);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<java.util.List<Review>> bookReviewList(@PathVariable Long bookId) {
        java.util.List<Review> reviews = reviewService.findReviewsByBook(bookId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReview(@PathVariable Long id) {
        Optional<Review> review = reviewService.getReview(id);
        return review.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @RequestBody Review review) {
        return ResponseEntity.ok(reviewService.updateReview(id, review));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<Review>> listReviews(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(reviewService.listReviews(pageable));
    }
} 