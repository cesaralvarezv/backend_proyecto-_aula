package com.backend_yireh_store.domain.model;

import lombok.*;
import java.math.BigDecimal;

/**
 * Domain model — Item del carrito.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CartItem {

    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal unitPrice;
    private int quantity;

    public BigDecimal getTotalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}