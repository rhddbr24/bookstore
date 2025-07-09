package com.book.bookstore.product.service;

import com.book.bookstore.product.domain.Review;
import com.book.bookstore.product.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    public Optional<Review> getReview(Long id) {
        return reviewRepository.findById(id);
    }

    public Review updateReview(Long id, Review review) {
        review.setId(id);
        return reviewRepository.save(review);
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    public Page<Review> listReviews(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }

    public java.util.List<Review> findReviewsByUser(com.book.bookstore.user.domain.User user) {
        return reviewRepository.findAll((root, query, cb) -> cb.equal(root.get("user"), user));
    }
    public java.util.List<Review> findReviewsByBook(Long bookId) {
        return reviewRepository.findAll((root, query, cb) -> cb.equal(root.get("book").get("id"), bookId));
    }
} 