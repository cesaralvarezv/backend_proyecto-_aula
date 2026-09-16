package com.backend_yireh_store.application.service;

import com.backend_yireh_store.domain.model.*;
import com.backend_yireh_store.domain.port.in.CartUseCase;
import com.backend_yireh_store.domain.port.in.OrderUseCase;
import com.backend_yireh_store.domain.port.in.WalletUseCase;
import com.backend_yireh_store.domain.port.out.OrderRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService implements OrderUseCase {

    private final OrderRepositoryPort orderRepository;
    private final CartUseCase cartUseCase;
    private final WalletUseCase walletUseCase;

    public OrderService(OrderRepositoryPort orderRepository,
                        CartUseCase cartUseCase,
                        WalletUseCase walletUseCase) {
        this.orderRepository = orderRepository;
        this.cartUseCase = cartUseCase;
        this.walletUseCase = walletUseCase;
    }

    @Override
    @Transactional
    public Order checkout(String userEmail, String paymentMethod, String deliveryLocation, String notes) {
        Cart cart = cartUseCase.getOrCreateCart(userEmail);
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new IllegalStateException("El carrito está vacío. Agrega productos antes de pagar.");
        }

        BigDecimal total = cart.getTotal();

        // Si paga con billetera, verificar y debitar saldo
        if ("WALLET".equalsIgnoreCase(paymentMethod)) {
            walletUseCase.debit(userEmail, total);
        }

        // Construir ítems de la orden desde el carrito
        List<OrderItem> orderItems = cart.getItems().stream().map(ci ->
                OrderItem.builder()
                        .productId(ci.getProductId())
                        .productName(ci.getProductName())
                        .unitPrice(ci.getUnitPrice())
                        .quantity(ci.getQuantity())
                        .build()
        ).collect(Collectors.toList());

        String orderNumber = orderRepository.generateOrderNumber();

        Order order = Order.builder()
                .orderNumber(orderNumber)
                .userEmail(userEmail)
                .items(orderItems)
                .total(total)
                .status(OrderStatus.PENDING)
                .paymentMethod(paymentMethod != null ? paymentMethod.toUpperCase() : "CASH")
                .deliveryLocation(deliveryLocation != null ? deliveryLocation : "")
                .notes(notes)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Order saved = orderRepository.save(order);

        // Vaciar carrito tras crear la orden
        cartUseCase.clear(userEmail);

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getMyOrders(String userEmail) {
        return orderRepository.findByUserEmail(userEmail);
    }

    @Override
    @Transactional(readOnly = true)
    public Order getById(Long id, String requesterEmail, boolean isAdmin) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado: " + id));
        if (!isAdmin && !order.getUserEmail().equalsIgnoreCase(requesterEmail)) {
            throw new SecurityException("No tienes permiso para ver este pedido.");
        }
        return order;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getAllOrders(OrderStatus statusFilter) {
        if (statusFilter != null) {
            return orderRepository.findByStatus(statusFilter);
        }
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public Order updateStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado: " + orderId));
        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }
}
