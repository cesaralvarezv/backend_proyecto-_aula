package com.backend_yireh_store.domain.port.out;

import com.backend_yireh_store.domain.model.Cart;

import java.util.Optional;

public interface CartRepositoryPort {
    Cart save(Cart cart);
    Optional<Cart> findByUserId(String userId);
    Optional<Cart> findById(Long id);
    void deleteById(Long id);
}