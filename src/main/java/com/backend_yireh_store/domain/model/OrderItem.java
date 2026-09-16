package com.backend_yireh_store.domain.model;

import lombok.*;
import java.math.BigDecimal;

/** Ítem dentro de un pedido — copia inmutable del producto al momento de la compra. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal unitPrice;
    private int quantity;

    public BigDecimal getSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
