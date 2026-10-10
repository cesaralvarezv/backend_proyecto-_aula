package com.backend_yireh_store.infrastructure.adapter.in.web.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashClosureResponse {

    private Long id;
    private Long cashierId;
    private String cashierEmail;
    private BigDecimal openingAmount;
    private LocalDateTime openingTime;
    private LocalDateTime closingTime;
    private BigDecimal cashSalesSystem;
    private BigDecimal virtualWalletSales;
    private BigDecimal cardSales;
    private BigDecimal totalSalesSystem;
    private BigDecimal declaredCash;
    private BigDecimal discrepancy;
    private String status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
