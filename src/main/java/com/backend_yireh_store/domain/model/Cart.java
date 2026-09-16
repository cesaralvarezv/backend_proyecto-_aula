package com.backend_yireh_store.domain.model;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Domain model — Carrito de compras del usuario.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Cart {

    private Long id;
    private String userId; // Email del usuario autenticado
    @Builder.Default
    private List<CartItem> items = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Cart createNew(String userId) {
        LocalDateTime now = LocalDateTime.now();
        return Cart.builder()
                .userId(userId)
                .items(new ArrayList<>())
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public void addItem(CartItem item) {
        // Buscar si ya existe el producto en el carrito
        for (CartItem existing : items) {
            if (existing.getProductId().equals(item.getProductId())) {
                existing.setQuantity(existing.getQuantity() + item.getQuantity());
                this.updatedAt = LocalDateTime.now();
                return;
            }
        }
        items.add(item);
        this.updatedAt = LocalDateTime.now();
    }

    public void removeItem(Long productId) {
        items.removeIf(item -> item.getProductId().equals(productId));
        this.updatedAt = LocalDateTime.now();
    }

    public void updateQuantity(Long productId, int quantity) {
        for (CartItem item : items) {
            if (item.getProductId().equals(productId)) {
                if (quantity <= 0) {
                    items.remove(item);
                } else {
                    item.setQuantity(quantity);
                }
                this.updatedAt = LocalDateTime.now();
                return;
            }
        }
    }

    public void clear() {
        items.clear();
        this.updatedAt = LocalDateTime.now();
    }

    public BigDecimal getTotal() {
        return items.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getItemCount() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }
}