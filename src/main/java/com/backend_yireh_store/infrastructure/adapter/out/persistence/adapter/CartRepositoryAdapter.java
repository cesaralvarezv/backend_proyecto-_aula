package com.backend_yireh_store.infrastructure.adapter.out.persistence.adapter;

import com.backend_yireh_store.domain.model.Cart;
import com.backend_yireh_store.domain.model.CartItem;
import com.backend_yireh_store.domain.port.out.CartRepositoryPort;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.CartEntity;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.CartItemEntity;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.repository.CartJpaRepository;
import com.backend_yireh_store.infrastructure.mapper.CartMapper;
import com.backend_yireh_store.infrastructure.mapper.CartMapperHelper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CartRepositoryAdapter implements CartRepositoryPort {

    private final CartJpaRepository cartJpaRepository;
    private final CartMapper cartMapper;
    private final CartMapperHelper cartMapperHelper;

    public CartRepositoryAdapter(CartJpaRepository cartJpaRepository, CartMapper cartMapper, CartMapperHelper cartMapperHelper) {
        this.cartJpaRepository = cartJpaRepository;
        this.cartMapper = cartMapper;
        this.cartMapperHelper = cartMapperHelper;
    }

    @Override
    public Cart save(Cart cart) {
        CartEntity entity = cartMapperHelper.toEntityWithItems(cart);
        CartEntity saved = cartJpaRepository.save(entity);
        return cartMapper.toDomain(saved);
    }

    @Override
    public Optional<Cart> findByUserId(String userId) {
        return cartJpaRepository.findByUserId(userId)
                .map(cartMapper::toDomain);
    }

    @Override
    public Optional<Cart> findById(Long id) {
        return cartJpaRepository.findById(id)
                .map(cartMapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        cartJpaRepository.deleteById(id);
    }
}