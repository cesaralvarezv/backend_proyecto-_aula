package com.backend_yireh_store.domain.port.in;

import com.backend_yireh_store.domain.model.Cart;
import com.backend_yireh_store.domain.model.CartItem;

import java.util.List;

public interface CartUseCase {
    Cart getOrCreateCart(String userId);
    Cart addItem(String userId, Long productId, String productName, java.math.BigDecimal unitPrice, int quantity);
    Cart removeItem(String userId, Long productId);
    Cart updateQuantity(String userId, Long productId, int quantity);
    Cart clear(String userId);
    List<CartItem> getItems(String userId);
    int getItemCount(String userId);
    java.math.BigDecimal getTotal(String userId);
}