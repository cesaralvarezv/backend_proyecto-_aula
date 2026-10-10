package com.backend_yireh_store.infrastructure.adapter.out.persistence.entity;

import com.backend_yireh_store.domain.model.CashClosureStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cash_closures")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashClosureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cashier_id", nullable = false)
    private Long cashierId;

    @Column(name = "cashier_email", nullable = false, length = 150)
    private String cashierEmail;

    @Column(name = "opening_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal openingAmount;

    @Column(name = "opening_time", nullable = false)
    private LocalDateTime openingTime;

    @Column(name = "closing_time")
    private LocalDateTime closingTime;

    @Column(name = "cash_sales_system", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal cashSalesSystem = BigDecimal.ZERO;

    @Column(name = "virtual_wallet_sales", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal virtualWalletSales = BigDecimal.ZERO;

    @Column(name = "card_sales", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal cardSales = BigDecimal.ZERO;

    @Column(name = "total_sales_system", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal totalSalesSystem = BigDecimal.ZERO;

    @Column(name = "declared_cash", precision = 12, scale = 2)
    private BigDecimal declaredCash;

    @Column(name = "discrepancy", precision = 12, scale = 2)
    private BigDecimal discrepancy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private CashClosureStatus status;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
