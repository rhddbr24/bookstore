package com.book.bookstore.product.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private String author;

    private String imageUrl;
    private String previewPdfUrl;

    @Lob
    private String description;

    @Column(nullable = false)
    private int price;

    private String size;
    private double rating;
    private int salesIndex;
    private int stockQuantity;

    @Enumerated(EnumType.STRING)
    private SaleStatus saleStatus;

    private LocalDateTime createdAt;

    public enum SaleStatus {
        SALE, SOLD_OUT, TEMP_OUT, COMING_SOON
    }
}
