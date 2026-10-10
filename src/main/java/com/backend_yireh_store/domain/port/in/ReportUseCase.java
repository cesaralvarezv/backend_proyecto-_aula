package com.backend_yireh_store.domain.port.in;

import com.backend_yireh_store.infrastructure.adapter.in.web.dto.ProjectionReportResponse;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.SalesReportResponse;

import java.time.LocalDate;

public interface ReportUseCase {

    SalesReportResponse generateSalesReport(LocalDate startDate, LocalDate endDate);

    ProjectionReportResponse generateWeeklyDemandProjection();
}
