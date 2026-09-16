package com.backend_yireh_store.application.service;

import com.backend_yireh_store.domain.model.Cart;
import com.backend_yireh_store.domain.model.CartItem;
import com.backend_yireh_store.domain.port.in.CartUseCase;
import com.backend_yireh_store.domain.port.out.CartRepositoryPort;
import com.backend_yireh_store.domain.port.out.ProductRepositoryPort;
import com.backend_yireh_store.domain.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService implements CartUseCase {

    private final CartRepositoryPort cartRepository;
    private final ProductRepositoryPort productRepository;

    public CartService(CartRepositoryPort cartRepository, ProductRepositoryPort productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Cart getOrCreateCart(String userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> cartRepository.save(Cart.createNew(userId)));
    }

    @Override
    @Transactional
    public Cart addItem(String userId, Long productId, String productName, BigDecimal unitPrice, int quantity) {
        // Verificar que el producto existe
        if (!productRepository.findById(productId).isPresent()) {
            throw new ProductNotFoundException(productId);
        }

        Cart cart = getOrCreateCart(userId);
        CartItem item = CartItem.builder()
                .productId(productId)
                .productName(productName)
                .unitPrice(unitPrice)
                .quantity(quantity)
                .build();
        cart.addItem(item);
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public Cart removeItem(String userId, Long productId) {
        Cart cart = getOrCreateCart(userId);
        cart.removeItem(productId);
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public Cart updateQuantity(String userId, Long productId, int quantity) {
        Cart cart = getOrCreateCart(userId);
        cart.updateQuantity(productId, quantity);
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public Cart clear(String userId) {
        Cart cart = getOrCreateCart(userId);
        cart.clear();
        return cartRepository.save(cart);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItem> getItems(String userId) {
        return getOrCreateCart(userId).getItems();
    }

    @Override
    @Transactional(readOnly = true)
    public int getItemCount(String userId) {
        return getOrCreateCart(userId).getItemCount();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotal(String userId) {
        return getOrCreateCart(userId).getTotal();
    }
}