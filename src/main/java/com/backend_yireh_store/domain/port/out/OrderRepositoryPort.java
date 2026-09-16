package com.backend_yireh_store.domain.port.out;

import com.backend_yireh_store.domain.model.Order;
import com.backend_yireh_store.domain.model.OrderStatus;
import java.util.List;
import java.util.Optional;

public interface OrderRepositoryPort {
    Order save(Order order);
    Optional<Order> findById(Long id);
    List<Order> findByUserEmail(String userEmail);
    List<Order> findAll();
    List<Order> findByStatus(OrderStatus status);
    /** Genera el próximo número de orden (ej: ORD-20260912-0001). */
    String generateOrderNumber();
}
