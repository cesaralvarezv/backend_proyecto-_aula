package com.backend_yireh_store.infrastructure.mapper;

import com.backend_yireh_store.domain.model.Cart;
import com.backend_yireh_store.domain.model.CartItem;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.CartItemResponse;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.CartResponse;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.CartEntity;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.CartItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "items", source = "items")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    Cart toDomain(CartEntity entity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "items", source = "items")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    CartEntity toEntity(Cart cart);

    default CartItem toDomain(CartItemEntity entity) {
        if (entity == null) return null;
        return CartItem.builder()
                .id(entity.getId())
                .productId(entity.getProductId())
                .productName(entity.getProductName())
                .unitPrice(entity.getUnitPrice())
                .quantity(entity.getQuantity())
                .build();
    }

    default CartItemEntity toEntity(CartItem item) {
        if (item == null) return null;
        return CartItemEntity.builder()
                .id(item.getId())
                .productId(item.getProductId())
                .productName(item.getProductName())
                .unitPrice(item.getUnitPrice())
                .quantity(item.getQuantity())
                .build();
    }

    default List<CartItem> toDomain(List<CartItemEntity> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(this::toDomain).collect(Collectors.toList());
    }

    default List<CartItemEntity> toEntity(List<CartItem> items) {
        if (items == null) return List.of();
        return items.stream().map(this::toEntity).collect(Collectors.toList());
    }

    CartResponse toResponse(Cart cart);

    default CartItemResponse toResponse(CartItem item) {
        return new CartItemResponse(
                item.getId(),
                item.getProductId(),
                item.getProductName(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getTotalPrice()
        );
    }

    default List<CartItemResponse> toResponse(List<CartItem> items) {
        if (items == null) return List.of();
        return items.stream().map(this::toResponse).collect(Collectors.toList());
    }
}