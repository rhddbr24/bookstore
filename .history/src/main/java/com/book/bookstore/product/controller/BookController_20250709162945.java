package com.book.bookstore.product.controller;

import com.book.bookstore.product.domain.Book;
import com.book.bookstore.product.service.BookService;
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
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.createBook(book));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        Optional<Book> book = bookService.getBook(id);
        return book.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        return ResponseEntity.ok(bookService.updateBook(id, book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<Book>> listBooks(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(bookService.listBooks(pageable));
    }

    @PostMapping("/admin")
    public ResponseEntity<Book> adminCreateBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.createBook(book));
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<Book> adminUpdateBook(@PathVariable Long id, @RequestBody Book book) {
        return ResponseEntity.ok(bookService.updateBook(id, book));
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> adminDeleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin")
    public ResponseEntity<Page<Book>> adminListBooks(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Integer stockQuantity,
            @RequestParam(required = false) Book.SaleStatus saleStatus,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort
    ) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, org.springframework.data.domain.Sort.by(parseSort(sort)));
        Specification<Book> spec = (root, query, cb) -> {
            java.util.List<jakarta.persistence.criteria.Predicate> predicates = new java.util.ArrayList<>();
            if (StringUtils.hasText(name)) {
                predicates.add(cb.like(root.get("name"), "%" + name + "%"));
            }
            if (StringUtils.hasText(publisher)) {
                predicates.add(cb.like(root.get("publisher"), "%" + publisher + "%"));
            }
            if (StringUtils.hasText(author)) {
                predicates.add(cb.like(root.get("author"), "%" + author + "%"));
            }
            if (stockQuantity != null) {
                predicates.add(cb.equal(root.get("stockQuantity"), stockQuantity));
            }
            if (saleStatus != null) {
                predicates.add(cb.equal(root.get("saleStatus"), saleStatus));
            }
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), startDate));
            }
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), endDate));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
        return ResponseEntity.ok(bookService.listBooksWithSearch(spec, pageable));
    }

    @GetMapping("/admin/stock")
    public ResponseEntity<Page<Book>> adminStockList(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size,
            @RequestParam(defaultValue = "name,asc") String[] sort
    ) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, org.springframework.data.domain.Sort.by(parseSort(sort)));
        Specification<Book> spec = (root, query, cb) -> {
            java.util.List<jakarta.persistence.criteria.Predicate> predicates = new java.util.ArrayList<>();
            if (StringUtils.hasText(name)) {
                predicates.add(cb.like(root.get("name"), "%" + name + "%"));
            }
            if (StringUtils.hasText(publisher)) {
                predicates.add(cb.like(root.get("publisher"), "%" + publisher + "%"));
            }
            if (StringUtils.hasText(author)) {
                predicates.add(cb.like(root.get("author"), "%" + author + "%"));
            }
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), startDate));
            }
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), endDate));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
        return ResponseEntity.ok(bookService.listBooksWithSearch(spec, pageable));
    }

    @GetMapping("/user")
    public ResponseEntity<Page<Book>> userListBooks(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        Specification<Book> spec = (root, query, cb) -> {
            java.util.List<jakarta.persistence.criteria.Predicate> predicates = new java.util.ArrayList<>();
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }
            if (org.springframework.util.StringUtils.hasText(keyword)) {
                predicates.add(cb.or(
                    cb.like(root.get("name"), "%" + keyword + "%"),
                    cb.like(root.get("publisher"), "%" + keyword + "%"),
                    cb.like(root.get("author"), "%" + keyword + "%")
                ));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
        return ResponseEntity.ok(bookService.listBooksWithSearch(spec, pageable));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Book> userGetBook(@PathVariable Long id, HttpSession session) {
        Optional<Book> bookOpt = bookService.getBook(id);
        if (bookOpt.isEmpty()) return ResponseEntity.notFound().build();
        // 최근 본 책 세션에 저장
        java.util.List<Long> ids = (java.util.List<Long>) session.getAttribute("recentlyViewed");
        if (ids == null) ids = new java.util.ArrayList<>();
        if (!ids.contains(id)) {
            ids.add(0, id);
            if (ids.size() > 10) ids = ids.subList(0, 10);
            session.setAttribute("recentlyViewed", ids);
        }
        return ResponseEntity.ok(bookOpt.get());
    }

    @GetMapping("/bestseller")
    public ResponseEntity<java.util.List<Book>> bestsellerBooks() {
        // 예시: 판매지수(salesIndex) 상위 10개
        Pageable pageable = org.springframework.data.domain.PageRequest.of(0, 10, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Order.desc("salesIndex")));
        Page<Book> page = bookService.listBooks(pageable);
        return ResponseEntity.ok(page.getContent());
    }

    @GetMapping("/popular-keywords")
    public ResponseEntity<java.util.List<String>> popularKeywords() {
        // 예시: 인기검색어는 별도 테이블/캐시 필요, 여기서는 더미 데이터 반환
        return ResponseEntity.ok(java.util.List.of("자바", "스프링", "JPA", "AI", "베스트셀러"));
    }

    @GetMapping("/recently-viewed")
    public ResponseEntity<java.util.List<Book>> recentlyViewedBooks(HttpSession session) {
        java.util.List<Long> ids = (java.util.List<Long>) session.getAttribute("recentlyViewed");
        if (ids == null || ids.isEmpty()) return ResponseEntity.ok(java.util.List.of());
        java.util.List<Book> books = bookService.findAllById(ids);
        return ResponseEntity.ok(books);
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