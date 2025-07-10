package com.book.bookstore.user.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.io.Serializable;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phone;
    private String address;

    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Grade {
        BASIC, SILVER, GOLD, VIP
    }

    public enum Status {
        ACTIVE, INACTIVE, SLEEP, WITHDRAWN
    }
}
