package com.backend_yireh_store.domain.model;

import lombok.*;
import java.time.LocalDateTime;

/**
 * Domain model — Categoría global de productos.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Category {

    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private String description;
    private String icon;
    private boolean active;
    private int order;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Category createNew(String name, String description, String icon, int order) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Nombre de categoría requerido");
        if (icon == null || icon.trim().isEmpty()) throw new IllegalArgumentException("Icono de categoría requerido");
        if (order < 0) throw new IllegalArgumentException("El orden no puede ser negativo");
        LocalDateTime now = LocalDateTime.now();
        return Category.builder()
                .name(name.trim())
                .description(description != null ? description.trim() : "")
                .icon(icon.trim())
                .active(true)
                .order(order)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public void update(String name, String description, String icon, Integer order) {
        if (name != null) this.name = name.trim();
        if (description != null) this.description = description.trim();
        if (icon != null) this.icon = icon.trim();
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