package com.backend_yireh_store.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhysicalAsset {

    private Long id;
    private String assetCode;
    private String name;
    private String category;
    private String location;
    private BigDecimal purchasePrice;
    private LocalDate purchaseDate;
    
    @Builder.Default
    private int usefulLifeMonths = 60;

    @Builder.Default
    private BigDecimal depreciationAccumulated = BigDecimal.ZERO;

    private String maintenanceSchedule;
    private LocalDate lastMaintenanceDate;
    private LocalDate nextMaintenanceDate;

    @Builder.Default
    private String status = "OPERATIONAL"; // OPERATIONAL, IN_MAINTENANCE, DECOMMISSIONED

    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PhysicalAsset createNew(String assetCode, String name, String category, String location,
                                          BigDecimal purchasePrice, LocalDate purchaseDate,
                                          int usefulLifeMonths, String maintenanceSchedule,
                                          LocalDate nextMaintenanceDate, String notes) {
        if (assetCode == null || assetCode.isBlank()) throw new IllegalArgumentException("La placa o código de activo es requerida.");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("El nombre del equipo es requerido.");
        if (category == null || category.isBlank()) throw new IllegalArgumentException("La categoría es requerida.");
        if (purchasePrice == null || purchasePrice.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("El precio de compra no puede ser negativo.");

        LocalDateTime now = LocalDateTime.now();
        return PhysicalAsset.builder()
                .assetCode(assetCode.trim().toUpperCase())
                .name(name.trim())
                .category(category.trim())
                .location(location != null ? location.trim() : "")
                .purchasePrice(purchasePrice)
                .purchaseDate(purchaseDate != null ? purchaseDate : LocalDate.now())
                .usefulLifeMonths(usefulLifeMonths > 0 ? usefulLifeMonths : 60)
                .depreciationAccumulated(BigDecimal.ZERO)
                .maintenanceSchedule(maintenanceSchedule != null ? maintenanceSchedule.trim() : "")
                .nextMaintenanceDate(nextMaintenanceDate)
                .status("OPERATIONAL")
                .notes(notes != null ? notes.trim() : "")
                .createdAt(now)
                .updatedAt(now)
                .build();
    }
}
