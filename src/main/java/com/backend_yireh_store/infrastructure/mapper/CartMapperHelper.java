package com.backend_yireh_store.infrastructure.mapper;

import com.backend_yireh_store.domain.model.Cart;
import com.backend_yireh_store.domain.model.CartItem;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.CartEntity;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.CartItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapperHelper {

    public CartEntity toEntityWithItems(Cart cart) {
        if (cart == null) return null;

        CartEntity entity = CartEntity.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .build();

        if (cart.getItems() != null) {
            List<CartItemEntity> itemEntities = cart.getItems().stream()
                    .map(item -> {
                        CartItemEntity itemEntity = CartItemEntity.builder()
                                .id(item.getId())
                                .productId(item.getProductId())
                                .productName(item.getProductName())
                                .unitPrice(item.getUnitPrice())
                                .quantity(item.getQuantity())
                                .cart(entity)
                                .build();
                        return itemEntity;
                    })
                    .collect(Collectors.toList());
            entity.setItems(itemEntities);
        }

        return entity;
    }
}