package com.backend_yireh_store.infrastructure.adapter.in.web.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetResponse {

    private Long id;
    private String assetCode;
    private String name;
    private String category;
    private String location;
    private BigDecimal purchasePrice;
    private LocalDate purchaseDate;
    private int usefulLifeMonths;
    private BigDecimal depreciationAccumulated;
    private String maintenanceSchedule;
    private LocalDate lastMaintenanceDate;
    private LocalDate nextMaintenanceDate;
    private String status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
