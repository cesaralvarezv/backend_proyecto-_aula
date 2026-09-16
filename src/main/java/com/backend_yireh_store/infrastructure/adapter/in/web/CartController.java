package com.backend_yireh_store.infrastructure.adapter.in.web;

import com.backend_yireh_store.domain.model.Cart;
import com.backend_yireh_store.domain.port.in.CartUseCase;
import com.backend_yireh_store.domain.port.in.ProductUseCase;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.CartItemRequest;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.CartResponse;
import com.backend_yireh_store.infrastructure.mapper.CartMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    private final CartUseCase cartUseCase;
    private final ProductUseCase productUseCase;
    private final CartMapper cartMapper;

    public CartController(CartUseCase cartUseCase, ProductUseCase productUseCase, CartMapper cartMapper) {
        this.cartUseCase = cartUseCase;
        this.productUseCase = productUseCase;
        this.cartMapper = cartMapper;
    }

    private String getCurrentUserId(Authentication authentication) {
        return authentication.getName(); // email del usuario autenticado
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartResponse> getCart(Authentication authentication) {
        String userId = getCurrentUserId(authentication);
        Cart cart = cartUseCase.getOrCreateCart(userId);
        return ResponseEntity.ok(cartMapper.toResponse(cart));
    }

    @PostMapping("/items")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartResponse> addItem(Authentication authentication,
                                                 @Valid @RequestBody CartItemRequest request) {
        String userId = getCurrentUserId(authentication);
        var product = productUseCase.getById(request.productId());
        Cart cart = cartUseCase.addItem(userId, product.getId(), product.getName(), product.getPrice(), request.quantity());
        return ResponseEntity.ok(cartMapper.toResponse(cart));
    }

    @DeleteMapping("/items/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartResponse> removeItem(Authentication authentication,
                                                    @PathVariable Long productId) {
        String userId = getCurrentUserId(authentication);
        Cart cart = cartUseCase.removeItem(userId, productId);
        return ResponseEntity.ok(cartMapper.toResponse(cart));
    }

    @PutMapping("/items/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartResponse> updateQuantity(Authentication authentication,
                                                        @PathVariable Long productId,
                                                        @RequestParam int quantity) {
        String userId = getCurrentUserId(authentication);
        Cart cart = cartUseCase.updateQuantity(userId, productId, quantity);
        return ResponseEntity.ok(cartMapper.toResponse(cart));
    }

    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartResponse> clearCart(Authentication authentication) {
        String userId = getCurrentUserId(authentication);
        Cart cart = cartUseCase.clear(userId);
        return ResponseEntity.ok(cartMapper.toResponse(cart));
    }
}