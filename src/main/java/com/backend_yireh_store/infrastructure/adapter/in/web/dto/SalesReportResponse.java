package com.backend_yireh_store.infrastructure.adapter.in.web.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesReportResponse {

    private LocalDate startDate;
    private LocalDate endDate;
    private int totalOrders;
    private BigDecimal totalSales;
    private BigDecimal averageTicket;
    private Map<String, BigDecimal> salesByPaymentMethod;
    private Map<String, Integer> topSellingProducts;
    private Map<String, BigDecimal> salesByDate;
}
