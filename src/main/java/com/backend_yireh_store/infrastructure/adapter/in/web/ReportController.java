package com.backend_yireh_store.infrastructure.adapter.in.web;

import com.backend_yireh_store.domain.port.in.ReportUseCase;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.ProjectionReportResponse;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.SalesReportResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    private final ReportUseCase reportUseCase;

    public ReportController(ReportUseCase reportUseCase) {
        this.reportUseCase = reportUseCase;
    }

    /**
     * GET /api/reports/sales
     * Consolidado financiero de ventas, ticket promedio y métodos de pago por rango de fechas.
     */
    @GetMapping("/sales")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SalesReportResponse> getSalesReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        SalesReportResponse report = reportUseCase.generateSalesReport(startDate, endDate);
        return ResponseEntity.ok(report);
    }

    /**
     * GET /api/reports/projections
     * Modelo predictivo de series temporales y sugerencias de compra para la semana entrante.
     */
    @GetMapping("/projections")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjectionReportResponse> getProjections() {
        ProjectionReportResponse projection = reportUseCase.generateWeeklyDemandProjection();
        return ResponseEntity.ok(projection);
    }
}
