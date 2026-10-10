package com.backend_yireh_store.application.service;

import com.backend_yireh_store.domain.model.Order;
import com.backend_yireh_store.domain.model.OrderItem;
import com.backend_yireh_store.domain.model.OrderStatus;
import com.backend_yireh_store.domain.port.in.ReportUseCase;
import com.backend_yireh_store.domain.port.out.OrderRepositoryPort;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.ProjectionReportResponse;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.SalesReportResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReportService implements ReportUseCase {

    private final OrderRepositoryPort orderRepository;

    public ReportService(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public SalesReportResponse generateSalesReport(LocalDate startDate, LocalDate endDate) {
        LocalDate start = (startDate != null) ? startDate : LocalDate.now().minusDays(30);
        LocalDate end = (endDate != null) ? endDate : LocalDate.now();

        List<Order> allOrders = orderRepository.findAll();

        // Filtrar pedidos del rango y que no estén cancelados
        List<Order> filtered = allOrders.stream()
                .filter(o -> o.getStatus() != OrderStatus.CANCELLED)
                .filter(o -> {
                    LocalDate orderDate = o.getCreatedAt().toLocalDate();
                    return !orderDate.isBefore(start) && !orderDate.isAfter(end);
                })
                .collect(Collectors.toList());

        int totalOrders = filtered.size();
        BigDecimal totalSales = filtered.stream()
                .map(Order::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageTicket = totalOrders > 0
                ? totalSales.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        // Desglose por método de pago
        Map<String, BigDecimal> salesByPayment = new HashMap<>();
        for (Order o : filtered) {
            String method = o.getPaymentMethod() != null ? o.getPaymentMethod().toUpperCase() : "OTRO";
            salesByPayment.merge(method, o.getTotal(), BigDecimal::add);
        }

        // Productos más vendidos
        Map<String, Integer> productQuantities = new HashMap<>();
        for (Order o : filtered) {
            if (o.getItems() != null) {
                for (OrderItem item : o.getItems()) {
                    productQuantities.merge(item.getProductName(), item.getQuantity(), Integer::sum);
                }
            }
        }

        // Ordenar top productos (hasta 10)
        Map<String, Integer> topProducts = productQuantities.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        // Ventas por día
        Map<String, BigDecimal> salesByDate = new TreeMap<>();
        for (Order o : filtered) {
            String dateStr = o.getCreatedAt().toLocalDate().toString();
            salesByDate.merge(dateStr, o.getTotal(), BigDecimal::add);
        }

        return SalesReportResponse.builder()
                .startDate(start)
                .endDate(end)
                .totalOrders(totalOrders)
                .totalSales(totalSales)
                .averageTicket(averageTicket)
                .salesByPaymentMethod(salesByPayment)
                .topSellingProducts(topProducts)
                .salesByDate(salesByDate)
                .build();
    }

    @Override
    public ProjectionReportResponse generateWeeklyDemandProjection() {
        // Tomar histórico de pedidos recientes
        List<Order> validOrders = orderRepository.findAll().stream()
                .filter(o -> o.getStatus() != OrderStatus.CANCELLED)
                .collect(Collectors.toList());

        BigDecimal totalHistorical = validOrders.stream()
                .map(Order::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Promedio diario estimado
        int totalOrdersCount = validOrders.size();
        BigDecimal dailyAverage = (totalOrdersCount > 0)
                ? totalHistorical.divide(BigDecimal.valueOf(Math.max(totalOrdersCount, 1)), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(15)) // estimación de 15 pedidos/día
                : BigDecimal.valueOf(150000);

        // Proyección por día de la semana entrante con factores típicos de cafetería
        Map<String, BigDecimal> dailyProjections = new LinkedHashMap<>();
        Map<DayOfWeek, Double> dayWeights = Map.of(
                DayOfWeek.MONDAY, 1.15,    // Lunes de alta afluencia
                DayOfWeek.TUESDAY, 1.05,
                DayOfWeek.WEDNESDAY, 1.10,
                DayOfWeek.THURSDAY, 1.00,
                DayOfWeek.FRIDAY, 1.25,    // Viernes pico estudiantil
                DayOfWeek.SATURDAY, 0.60   // Sábado media jornada
        );

        BigDecimal projectedWeeklyTotal = BigDecimal.ZERO;
        for (Map.Entry<DayOfWeek, Double> entry : dayWeights.entrySet()) {
            String dayName = entry.getKey().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es"));
            BigDecimal dayForecast = dailyAverage.multiply(BigDecimal.valueOf(entry.getValue())).setScale(2, RoundingMode.HALF_UP);
            dailyProjections.put(capitalize(dayName), dayForecast);
            projectedWeeklyTotal = projectedWeeklyTotal.add(dayForecast);
        }

        // Sugerencias de compras basadas en los productos con mayor rotación
        Map<String, Integer> productQuantities = new HashMap<>();
        for (Order o : validOrders) {
            if (o.getItems() != null) {
                for (OrderItem item : o.getItems()) {
                    productQuantities.merge(item.getProductName(), item.getQuantity(), Integer::sum);
                }
            }
        }

        List<ProjectionReportResponse.ProductDemandProjection> suggestedPurchases = productQuantities.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(6)
                .map(entry -> {
                    int weeklyEstimated = (int) Math.ceil(entry.getValue() * 1.15); // proyección +15% de buffer
                    return ProjectionReportResponse.ProductDemandProjection.builder()
                            .productName(entry.getKey())
                            .estimatedWeeklyDemand(weeklyEstimated)
                            .recommendedReorderAction("Reabastecer stock preventivo: " + weeklyEstimated + " unidades estimadas.")
                            .build();
                })
                .collect(Collectors.toList());

        return ProjectionReportResponse.builder()
                .period("Semana entrante (7 días)")
                .projectedTotalRevenue(projectedWeeklyTotal)
                .projectedTotalOrders((int) Math.ceil(totalOrdersCount * 1.2))
                .dailyProjections(dailyProjections)
                .suggestedPurchases(suggestedPurchases)
                .build();
    }

    private String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
