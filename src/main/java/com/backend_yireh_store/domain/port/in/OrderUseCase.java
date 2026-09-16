package com.backend_yireh_store.domain.port.in;

import com.backend_yireh_store.domain.model.Order;
import com.backend_yireh_store.domain.model.OrderStatus;
import java.util.List;

public interface OrderUseCase {
    /** Crea una orden desde el carrito activo del usuario. Vacía el carrito. */
    Order checkout(String userEmail, String paymentMethod, String deliveryLocation, String notes);

    /** Historial de pedidos del usuario autenticado. */
    List<Order> getMyOrders(String userEmail);

    /** Detalle de un pedido (el usuario solo puede ver los suyos; admin ve todos). */
    Order getById(Long id, String requesterEmail, boolean isAdmin);

    /** Solo ADMIN — lista todos los pedidos con filtro opcional de estado. */
    List<Order> getAllOrders(OrderStatus statusFilter);

    /** Solo ADMIN — actualiza el estado de un pedido. */
    Order updateStatus(Long orderId, OrderStatus newStatus);
}
