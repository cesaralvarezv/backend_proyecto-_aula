package com.backend_yireh_store.infrastructure.adapter.in.web;

import com.backend_yireh_store.domain.model.Order;
import com.backend_yireh_store.domain.model.OrderItem;
import com.backend_yireh_store.domain.model.OrderStatus;
import com.backend_yireh_store.domain.port.in.OrderUseCase;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.CheckoutRequest;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.OrderItemResponse;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.OrderResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderUseCase orderUseCase;

    public OrderController(OrderUseCase orderUseCase) {
        this.orderUseCase = orderUseCase;
    }

    /** POST /api/orders/checkout — confirmar compra desde el carrito. */
    @PostMapping("/checkout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderResponse> checkout(Authentication auth,
                                                   @RequestBody CheckoutRequest request) {
        String email = auth.getName();
        Order order = orderUseCase.checkout(email, request.paymentMethod(),
                request.deliveryLocation(), request.notes());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(order));
    }

    /** GET /api/orders/my-orders — historial del usuario autenticado. */
    @GetMapping("/my-orders")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<OrderResponse>> getMyOrders(Authentication auth) {
        String email = auth.getName();
        List<OrderResponse> orders = orderUseCase.getMyOrders(email)
                .stream().map(this::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    /** GET /api/orders/{id} — detalle de un pedido. */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderResponse> getById(@PathVariable Long id, Authentication auth) {
        String email = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.ok(toResponse(orderUseCase.getById(id, email, isAdmin)));
    }

    /** GET /api/orders/admin — todos los pedidos (solo ADMIN). */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponse>> getAllOrders(
            @RequestParam(required = false) String status) {
        OrderStatus statusFilter = null;
        if (status != null && !status.isBlank()) {
            try { statusFilter = OrderStatus.valueOf(status.toUpperCase()); }
            catch (IllegalArgumentException ignored) {}
        }
        List<OrderResponse> orders = orderUseCase.getAllOrders(statusFilter)
                .stream().map(this::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    /** PATCH /api/orders/{id}/status — actualizar estado de pedido (solo ADMIN). */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable Long id,
                                                       @RequestParam String status) {
        OrderStatus newStatus = OrderStatus.valueOf(status.toUpperCase());
        return ResponseEntity.ok(toResponse(orderUseCase.updateStatus(id, newStatus)));
    }

    // ---- Helpers ----
    private OrderResponse toResponse(Order o) {
        List<OrderItemResponse> items = o.getItems() == null ? List.of()
                : o.getItems().stream().map(this::toItemResponse).collect(Collectors.toList());
        return new OrderResponse(o.getId(), o.getOrderNumber(), o.getUserEmail(),
                items, o.getTotal(), o.getStatus().name(), o.getPaymentMethod(),
                o.getDeliveryLocation(), o.getNotes(), o.getCreatedAt(), o.getUpdatedAt());
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        return new OrderItemResponse(item.getProductId(), item.getProductName(),
                item.getUnitPrice(), item.getQuantity(), item.getSubtotal());
    }
}
