package com.backend_yireh_store.domain.model;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/** Pedido realizado por un usuario. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    private Long id;
    private String orderNumber;      // Ej: ORD-20260912-0001
    private String userEmail;
    private List<OrderItem> items;
    private BigDecimal total;
    private OrderStatus status;
    private String paymentMethod;    // WALLET, CASH, NEQUI, DAVIPLATA
    private String deliveryLocation; // Ej: "Piso 3 - Local 302"
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
