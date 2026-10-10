package com.backend_yireh_store.infrastructure.adapter.in.web.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectionReportResponse {

    private String period; // Ej: "Siguiente Semana"
    private BigDecimal projectedTotalRevenue;
    private int projectedTotalOrders;
    private Map<String, BigDecimal> dailyProjections; // Lunes a Sábado proyectado
    private List<ProductDemandProjection> suggestedPurchases; // Insumos y productos clave

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductDemandProjection {
        private String productName;
        private int estimatedWeeklyDemand;
        private String recommendedReorderAction;
    }
}
