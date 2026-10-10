package com.backend_yireh_store.infrastructure.adapter.in.web;

import com.backend_yireh_store.domain.model.Order;
import com.backend_yireh_store.domain.model.OrderItem;
import com.backend_yireh_store.domain.port.in.PosUseCase;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.OrderItemResponse;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.OrderResponse;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.PosCheckoutRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pos")
@CrossOrigin(origins = "*")
public class PosController {

    private final PosUseCase posUseCase;

    public PosController(PosUseCase posUseCase) {
        this.posUseCase = posUseCase;
    }

    /**
     * POST /api/pos/checkout
     * Registro de venta directa física en caja mostrador.
     */
    @PostMapping("/checkout")
    @PreAuthorize("hasAnyRole('CASHIER', 'ADMIN')")
    public ResponseEntity<OrderResponse> checkout(
            Authentication auth,
            @Valid @RequestBody PosCheckoutRequest request) {

        String cashierEmail = auth.getName();
        Order order = posUseCase.processPosCheckout(cashierEmail, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(order));
    }

    private OrderResponse toResponse(Order o) {
        List<OrderItemResponse> items = o.getItems() == null ? List.of()
                : o.getItems().stream().map(this::toItemResponse).collect(Collectors.toList());

        return new OrderResponse(
                o.getId(),
                o.getOrderNumber(),
                o.getUserEmail(),
                items,
                o.getTotal(),
                o.getStatus() != null ? o.getStatus().name() : null,
                o.getPaymentMethod(),
                o.getDeliveryLocation(),
                o.getNotes(),
                o.getOrderType(),
                o.getCashierId(),
                o.getClosureId(),
                o.getCashReceived(),
                o.getChangeReturned(),
                o.getCreatedAt(),
                o.getUpdatedAt()
        );
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getProductId(),
                item.getProductName(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getSubtotal()
        );
    }
}
