package com.backend_yireh_store.domain.model;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain model — Producto de la cafetería.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "category")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product {

    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private String imageUrl;
    @EqualsAndHashCode.Exclude
    private Category category;
    private boolean active;
    private int order;
    @Builder.Default
    private int quantity = 0;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void reduceQuantity(int amount) {
        if (amount <= 0) return;
        if (this.quantity < amount) {
            throw new IllegalStateException("Stock insuficiente para el producto: " + this.name
                    + ". Disponible: " + this.quantity + ", Solicitado: " + amount);
        }
        this.quantity -= amount;
        this.updatedAt = LocalDateTime.now();
    }

    public void increaseQuantity(int amount) {
        if (amount <= 0) return;
        this.quantity += amount;
        this.updatedAt = LocalDateTime.now();
    }

    public static Product createNew(String name, BigDecimal price, String description, String imageUrl,
                                    Category category, int order) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Nombre de producto requerido");
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Precio debe ser mayor a cero");
        if (category == null) throw new IllegalArgumentException("Categoría requerida");
        if (order < 0) throw new IllegalArgumentException("El orden no puede ser negativo");
        LocalDateTime now = LocalDateTime.now();
        return Product.builder()
                .name(name.trim())
                .price(price)
                .description(description != null ? description.trim() : "")
                .imageUrl(imageUrl != null ? imageUrl.trim() : "")
                .category(category)
                .active(true)
                .order(order)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public void update(String name, BigDecimal price, String description, String imageUrl,
                       Category category, Integer order) {
        if (name != null) this.name = name.trim();
        if (price != null) this.price = price;
        if (description != null) this.description = description.trim();
        if (imageUrl != null) this.imageUrl = imageUrl.trim();
        if (category != null) this.category = category;
        if (order != null) this.order = order;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }
}