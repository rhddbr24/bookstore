package com.book.bookstore.order.domain;

import com.book.bookstore.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    private int totalAmount;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime orderDate;

    public enum Status {
        ORDERED, PAID, SHIPPED, CANCELLED, COMPLETED
    }
}
