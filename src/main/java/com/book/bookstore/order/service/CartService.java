package com.book.bookstore.order.service;

import com.book.bookstore.order.domain.Cart;
import com.book.bookstore.order.domain.CartItem;
import com.book.bookstore.product.domain.Book;
import com.book.bookstore.user.domain.User;
import com.book.bookstore.order.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    public Cart createCart(Cart cart) {
        return cartRepository.save(cart);
    }

    public Optional<Cart> getCart(Long id) {
        return cartRepository.findById(id);
    }

    public Cart updateCart(Long id, Cart cart) {
        cart.setId(id);
        return cartRepository.save(cart);
    }

    public void deleteCart(Long id) {
        cartRepository.deleteById(id);
    }

    public Page<Cart> listCarts(Pageable pageable) {
        return cartRepository.findAll(pageable);
    }

    public Cart findOrCreateCartByUser(User user) {
        return cartRepository.findOne((root, query, cb) -> cb.equal(root.get("user"), user)).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setUser(user);
            cart.setCreatedAt(java.time.LocalDateTime.now());
            return cartRepository.save(cart);
        });
    }

    public void addBookToCart(Cart cart, Book book, int quantity) {
        CartItem item = cart.getCartItems().stream()
            .filter(ci -> ci.getBook().getId().equals(book.getId()))
            .findFirst()
            .orElse(null);
        if (item == null) {
            item = CartItem.builder().cart(cart).book(book).quantity(quantity).build();
            cart.getCartItems().add(item);
        } else {
            item.setQuantity(item.getQuantity() + quantity);
        }
        cartRepository.save(cart);
    }

    public void deleteCartItem(Long cartItemId, User user) {
        Cart cart = findOrCreateCartByUser(user);
        cart.getCartItems().removeIf(item -> item.getId().equals(cartItemId));
        cartRepository.save(cart);
    }
} 