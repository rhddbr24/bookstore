package com.book.bookstore.product.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
//@Table(name = "book")
public class book {
    @Id
    @Column(name = "booknum", nullable = false, length = 30)
    private int booknum;

    @Column(name = "bookname", nullable = false, length = 100)
    private String bookname;

    @Column(name = "publisher", nullable = false, length = 50)
    private String publisher;

    @Column(name = "writer", nullable = false, length = 30)
    private String writer;

    @Column(name = "bookcount", nullable = false)
    private long bookcount;

    @Column(name = "salestate", nullable = false, length = 10)
    private String salestate;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "book_regist", nullable = false)
    private Instant book_regist;

    @Column(name = "ISBN", nullable = false)
    private String isbn;

    @Column(name = "book_image", nullable = false)
    private String book_image;

    @Column(name = "preview", nullable = false)
    private String preview;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "")




}
