package com.backend_yireh_store.infrastructure.mapper;

import com.backend_yireh_store.domain.model.Order;
import com.backend_yireh_store.domain.model.OrderItem;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.OrderEntity;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.OrderItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/** Mapper manual para pedidos — evita la complejidad de relaciones bidireccionales en MapStruct. */
@Component
public class OrderMapper {

    public Order toDomain(OrderEntity entity) {
        if (entity == null) return null;
        List<OrderItem> items = entity.getItems() == null ? List.of()
                : entity.getItems().stream().map(this::itemToDomain).collect(Collectors.toList());
        return Order.builder()
                .id(entity.getId())
                .orderNumber(entity.getOrderNumber())
                .userEmail(entity.getUserEmail())
                .items(items)
                .total(entity.getTotal())
                .status(entity.getStatus())
                .paymentMethod(entity.getPaymentMethod())
                .deliveryLocation(entity.getDeliveryLocation())
                .notes(entity.getNotes())
                .orderType(entity.getOrderType())
                .cashierId(entity.getCashierId())
                .closureId(entity.getClosureId())
                .cashReceived(entity.getCashReceived())
                .changeReturned(entity.getChangeReturned())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public OrderEntity toEntity(Order domain) {
        OrderEntity entity = OrderEntity.builder()
                .id(domain.getId())
                .orderNumber(domain.getOrderNumber())
                .userEmail(domain.getUserEmail())
                .total(domain.getTotal())
                .status(domain.getStatus())
                .paymentMethod(domain.getPaymentMethod())
                .deliveryLocation(domain.getDeliveryLocation())
                .notes(domain.getNotes())
                .orderType(domain.getOrderType() != null ? domain.getOrderType() : "MOBILE")
                .cashierId(domain.getCashierId())
                .closureId(domain.getClosureId())
                .cashReceived(domain.getCashReceived())
                .changeReturned(domain.getChangeReturned())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();

        if (domain.getItems() != null) {
            List<OrderItemEntity> itemEntities = domain.getItems().stream()
                    .map(item -> itemToEntity(item, entity))
                    .collect(Collectors.toList());
            entity.setItems(itemEntities);
        }
        return entity;
    }

    private OrderItem itemToDomain(OrderItemEntity e) {
        return OrderItem.builder()
                .id(e.getId())
                .productId(e.getProductId())
                .productName(e.getProductName())
                .unitPrice(e.getUnitPrice())
                .quantity(e.getQuantity())
                .build();
    }

    private OrderItemEntity itemToEntity(OrderItem item, OrderEntity parentOrder) {
        return OrderItemEntity.builder()
                .id(item.getId())
                .order(parentOrder)
                .productId(item.getProductId())
                .productName(item.getProductName())
                .unitPrice(item.getUnitPrice())
                .quantity(item.getQuantity())
                .build();
    }
}
