package com.backend_yireh_store.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetRequest {

    @NotBlank(message = "La placa o código de activo es requerida")
    private String assetCode;

    @NotBlank(message = "El nombre del equipo es requerido")
    private String name;

    @NotBlank(message = "La categoría es requerida")
    private String category;

    private String location;

    @NotNull(message = "El precio de adquisición es requerido")
    @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
    private BigDecimal purchasePrice;

    private LocalDate purchaseDate;

    private Integer usefulLifeMonths;

    private String maintenanceSchedule;

    private LocalDate lastMaintenanceDate;

    private LocalDate nextMaintenanceDate;

    private String status;

    private String notes;
}
