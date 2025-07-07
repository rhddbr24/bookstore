package com.book.bookstore.user.domain;

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
@Table(name = "user")
public class User {
    @Id
    @Column(name = "id", nullable = false, length = 20)
    private String id;

    @Column(name = "password", nullable = false, length = 20)
    private String password;

    @Column(name = "email", nullable = false, length = 30)
    private String email;

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    @Column(name = "rank", nullable = false, length = 10)
    private String rank;

    @Column(name = "address", nullable = false, length = 50)
    private String address;

    @Column(name = "state", nullable = false, length = 10)
    private String state;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "register_at", nullable = false)
    private Instant registerAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "lastlogin_at", nullable = false)
    private Instant lastloginAt;
}
